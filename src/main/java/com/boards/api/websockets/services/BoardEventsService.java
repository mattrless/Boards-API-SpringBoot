package com.boards.api.websockets.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.boards.api.boards.entities.BoardMember;
import com.boards.api.websockets.dtos.events.BoardChangedDto;
import com.boards.api.websockets.events.BoardMemberWsEvent;
import com.boards.api.websockets.events.BoardWsEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardEventsService {
  private final SimpMessagingTemplate messagingTemplate;


  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onBoardEvent(BoardWsEvent event) {
    for (BoardMember member : event.boardMembers()) {
      messagingTemplate.convertAndSendToUser(
        member.getUser().getId().toString(),
        "/queue/boards",
        new BoardChangedDto(event.event(), event.boardId())
      );
    }
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onBoardMemberEvent(BoardMemberWsEvent event) {    
    messagingTemplate.convertAndSendToUser(
      event.targetUserId().toString(),
      "/queue/boards",
      new BoardChangedDto(event.event(), event.boardId())
    );    
  }
}

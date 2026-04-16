package com.boards.api.websockets.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.boards.api.boards.entities.BoardMember;
import com.boards.api.websockets.dtos.events.ListChangedDto;
import com.boards.api.websockets.events.BoardListWsEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardListEventsService {
  private final SimpMessagingTemplate messagingTemplate;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onBoardListEvent(BoardListWsEvent event) {
    for (BoardMember member : event.boardMembers()) {
      messagingTemplate.convertAndSendToUser(
        member.getUser().getId().toString(),
        "/queue/boards",
        new ListChangedDto(event.event(), event.boardId(), event.boardListId())
      );
    }
  }

}

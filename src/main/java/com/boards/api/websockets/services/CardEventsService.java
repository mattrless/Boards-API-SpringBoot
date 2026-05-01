package com.boards.api.websockets.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.boards.api.boards.entities.BoardMember;
import com.boards.api.websockets.dtos.events.CardChangedDto;
import com.boards.api.websockets.events.CardWsEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardEventsService {
  private final SimpMessagingTemplate messagingTemplate;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onCardEvent(CardWsEvent event) {
    for (BoardMember member : event.boardMembers()) {
      messagingTemplate.convertAndSendToUser(
        member.getUser().getId().toString(),
        "/queue/boards",
        new CardChangedDto(event.event(), event.boardId(), event.cardId(), event.targetBoardList(), event.sourceBoardListId(), event.userId())
      );
    }
  }
}

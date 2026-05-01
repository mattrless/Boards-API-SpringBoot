package com.boards.api.websockets.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardChangedDto {
  private String event;
  private Long boardId;
  private Long cardId;
  private Long targetBoardList;
  private Long sourceBoardListId;
  private Long userId;
}

package com.boards.api.websockets.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListChangedDto {
  private String event;
  private Long boardId;
  private Long boardListId;
}

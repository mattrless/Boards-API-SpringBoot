package com.boards.api.websockets.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardChangedDto {
  private String event;
  private Long boardId;
}

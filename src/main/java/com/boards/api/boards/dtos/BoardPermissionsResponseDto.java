package com.boards.api.boards.dtos;

import java.util.Set;

import lombok.Data;

@Data
public class BoardPermissionsResponseDto {
  private Long boardId;
  private String boardRole;
  private boolean ownedByCurrentUser;
  private Set<String> permissions;
}

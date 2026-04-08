package com.boards.api.boards.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BoardResponseDto {
  private Long id;
  private String name;
  private boolean ownedByCurrentUser;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

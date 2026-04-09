package com.boards.api.boardlists.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BoardListResponseDto {
  private Long id;
  private String title;
  private BigDecimal position;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

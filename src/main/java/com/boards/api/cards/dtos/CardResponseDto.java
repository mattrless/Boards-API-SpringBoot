package com.boards.api.cards.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CardResponseDto {
  private Long id;
  private String title;
  private String description;
  private BigDecimal position;  
}

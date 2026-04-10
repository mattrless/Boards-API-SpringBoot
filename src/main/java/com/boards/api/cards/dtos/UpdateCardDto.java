package com.boards.api.cards.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCardDto {
  @NotBlank
  private String title;

  @Size(max = 1000)
  private String description;
}

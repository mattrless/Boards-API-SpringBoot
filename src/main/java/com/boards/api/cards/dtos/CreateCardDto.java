package com.boards.api.cards.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCardDto {
  @NotBlank
  private String title;

  @Size(max = 1000)
  private String description;
}

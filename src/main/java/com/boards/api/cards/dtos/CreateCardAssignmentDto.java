package com.boards.api.cards.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCardAssignmentDto {  
  @NotNull
  @Positive
  private Long userId;
}

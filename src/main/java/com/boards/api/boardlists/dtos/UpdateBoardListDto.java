package com.boards.api.boardlists.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateBoardListDto {
  @NotBlank
  private String title;
}

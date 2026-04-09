package com.boards.api.boardlists.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBoardListDto {
  @NotBlank
  private String title;
}

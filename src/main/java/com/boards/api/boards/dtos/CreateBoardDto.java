package com.boards.api.boards.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBoardDto {
  @NotBlank
  private String name;
}

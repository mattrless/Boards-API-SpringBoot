package com.boards.api.ai.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateDescriptionDto {
  @NotBlank
  private String title;
}

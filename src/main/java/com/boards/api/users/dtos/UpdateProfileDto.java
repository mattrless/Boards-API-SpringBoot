package com.boards.api.users.dtos;

import org.hibernate.validator.constraints.URL;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileDto {
  @Schema(example = "John Doe")
  @Size(min = 1)
  @Pattern(regexp = ".*\\S.*")
  private String name;

  @Schema(example = "https://example.com/avatar.jpg")
  @URL
  private String avatar;
}

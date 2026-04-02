package com.boards.api.users.dtos;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileDto {
  @Size(min = 1)
  @Pattern(regexp = ".*\\S.*")
  private String name;

  @URL
  private String avatar;
}

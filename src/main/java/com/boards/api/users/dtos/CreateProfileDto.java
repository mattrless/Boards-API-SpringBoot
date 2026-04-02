package com.boards.api.users.dtos;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProfileDto {
  @NotBlank
  private String name;

  @URL
  private String avatar;
}

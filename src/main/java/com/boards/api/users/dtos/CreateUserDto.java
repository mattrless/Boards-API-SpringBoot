package com.boards.api.users.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {
  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 4, max = 255)
  private String password;

  @NotNull
  @Valid
  private CreateProfileDto profile;
}

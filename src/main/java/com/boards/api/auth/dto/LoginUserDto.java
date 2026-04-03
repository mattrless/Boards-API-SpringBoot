package com.boards.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserDto {
  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 4, max = 255)
  private String password;
}

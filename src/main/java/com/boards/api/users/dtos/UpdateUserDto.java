package com.boards.api.users.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {
  @Schema(example = "newemail@boards.com")
  @Email
  @Pattern(regexp = ".*\\S.*", message = "must not be blank")
  private String email;

  @Schema(example = "newpassword123")
  @Size(min = 4, max = 255)
  @Pattern(regexp = ".*\\S.*", message = "must not be blank")
  private String password;
  
  @Valid
  private UpdateProfileDto profile;
}

package com.boards.api.users.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {    
  @Email
  @Pattern(regexp = ".*\\S.*", message = "must not be blank")
  private String email;

  @Size(min = 4, max = 255)
  @Pattern(regexp = ".*\\S.*", message = "must not be blank")
  private String password;
  
  @Valid
  private UpdateProfileDto profile;
}

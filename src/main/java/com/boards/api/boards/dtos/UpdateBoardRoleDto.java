package com.boards.api.boards.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateBoardRoleDto {
  @Pattern(regexp = "admin|member", message = "role must be admin or member")
  private String role;
}

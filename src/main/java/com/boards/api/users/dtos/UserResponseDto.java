package com.boards.api.users.dtos;

import lombok.Data;

import java.time.LocalDateTime;

import com.boards.api.authorization.dtos.SystemRoleResponseDto;

@Data
public class UserResponseDto {
  private Long id;
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private ProfileResponseDto profile;
  private SystemRoleResponseDto systemRole;
}

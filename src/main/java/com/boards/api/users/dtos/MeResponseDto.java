package com.boards.api.users.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

import com.boards.api.authorization.dtos.SystemRoleResponseDto;

@Data
public class MeResponseDto {
  private Long id;
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private ProfileResponseDto profile;
  private SystemRoleResponseDto systemRole;
  private Set<String> permissions;
}

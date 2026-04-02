package com.boards.api.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponseDto {
  private Long id;
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private ProfileResponseDto profile;
}

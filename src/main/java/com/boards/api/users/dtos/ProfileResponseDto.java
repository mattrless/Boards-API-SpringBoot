package com.boards.api.users.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProfileResponseDto {
  private Long id;
  private String name;
  private String avatar;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

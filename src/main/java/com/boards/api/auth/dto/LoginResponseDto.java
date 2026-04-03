package com.boards.api.auth.dto;

import com.boards.api.users.dtos.UserResponseDto;

import lombok.Data;

@Data
public class LoginResponseDto {
  private UserResponseDto user;
  private String access_token;
}

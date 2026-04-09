package com.boards.api.users.dtos;

import lombok.Data;

@Data
public class UserSummaryResponseDto {
  private Long id;
  private String email;
  private ProfileResponseDto profile;  
}

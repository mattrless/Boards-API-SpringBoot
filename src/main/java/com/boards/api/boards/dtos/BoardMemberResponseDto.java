package com.boards.api.boards.dtos;

import com.boards.api.users.dtos.UserSummaryResponseDto;

import lombok.Data;

@Data
public class BoardMemberResponseDto {
  private UserSummaryResponseDto user;
  private BoardRoleSummaryResponseDto boardRole;
  private boolean owner;
}

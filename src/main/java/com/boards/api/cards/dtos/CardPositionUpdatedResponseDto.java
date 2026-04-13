package com.boards.api.cards.dtos;

import com.boards.api.boardlists.dtos.BoardListResponseDto;

import lombok.Data;

@Data
public class CardPositionUpdatedResponseDto {
  private CardResponseDto card;
  private BoardListResponseDto targetBoardList;
  private Long sourceBoardListId;
}

package com.boards.api.boardlists.mappers;

import org.mapstruct.Mapper;

import com.boards.api.boardlists.dtos.BoardListResponseDto;
import com.boards.api.boardlists.dtos.CreateBoardListDto;
import com.boards.api.boardlists.entities.BoardList;

@Mapper(componentModel = "spring")
public interface BoardListMapper {
  BoardList toEntity(CreateBoardListDto createBoardListDto);

  BoardListResponseDto toResponseDto(BoardList boardList);
}

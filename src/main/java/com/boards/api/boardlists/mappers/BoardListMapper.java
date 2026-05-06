package com.boards.api.boardlists.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.boards.api.boardlists.dtos.BoardListResponseDto;
import com.boards.api.boardlists.dtos.CreateBoardListDto;
import com.boards.api.boardlists.entities.BoardList;

@Mapper(componentModel = "spring")
public interface BoardListMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "position", ignore = true)
  @Mapping(target = "board", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "cards", ignore = true)
  BoardList toEntity(CreateBoardListDto createBoardListDto);

  BoardListResponseDto toResponseDto(BoardList boardList);
}

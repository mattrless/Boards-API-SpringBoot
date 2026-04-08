package com.boards.api.boards.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.boards.api.boards.dtos.BoardPermissionsResponseDto;
import com.boards.api.boards.dtos.BoardResponseDto;
import com.boards.api.boards.dtos.CreateBoardDto;
import com.boards.api.boards.entities.Board;
import com.boards.api.boards.entities.BoardMember;

@Mapper(componentModel = "spring")
public interface BoardMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)  
  Board toEntity(CreateBoardDto createBoardDto);
  
  @Mapping(target = "ownedByCurrentUser", source = "ownedByCurrentUser")
  BoardResponseDto toResponseDto(Board board, boolean ownedByCurrentUser);

  @Mapping(target = "boardId", source = "boardMember.board.id")
  @Mapping(target = "boardRole", source = "boardMember.boardRole.name")  
  @Mapping(target = "ownedByCurrentUser", source = "ownedByCurrentUser")
  BoardPermissionsResponseDto toBoardPermissionsResponseDto(
    BoardMember boardMember,
    boolean ownedByCurrentUser
  );

}

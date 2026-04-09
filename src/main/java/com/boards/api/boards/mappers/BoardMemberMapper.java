package com.boards.api.boards.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.boards.api.authorization.entities.BoardRole;
import com.boards.api.boards.dtos.BoardMemberResponseDto;
import com.boards.api.boards.entities.Board;
import com.boards.api.boards.entities.BoardMember;
import com.boards.api.users.entities.User;

@Mapper(componentModel = "spring")
public interface BoardMemberMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  BoardMember toEntity(User user, BoardRole boardRole, Board board);

  @Mapping(target = "user", source = "boardMember.user")
  @Mapping(target = "boardRole", source = "boardMember.boardRole")
  BoardMemberResponseDto toBoardMemberResponseDto(BoardMember boardMember, boolean owner);
}

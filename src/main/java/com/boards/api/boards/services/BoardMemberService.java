package com.boards.api.boards.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.boards.api.authorization.entities.BoardRole;
import com.boards.api.authorization.services.BoardRoleService;
import com.boards.api.boards.dtos.AddBoardMemberDto;
import com.boards.api.boards.entities.Board;
import com.boards.api.boards.entities.BoardMember;
import com.boards.api.boards.mappers.BoardMemberMapper;
import com.boards.api.boards.repositories.BoardMemberRepository;
import com.boards.api.boards.repositories.BoardRepository;
import com.boards.api.users.entities.User;
import com.boards.api.users.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardMemberService {
  private final UserRepository userRepository;
  private final BoardRepository boardRepository;
  private final BoardMemberRepository boardMemberRepository;
  private final BoardRoleService boardRoleService;
  private final BoardMemberMapper boardMemberMapper;  

  public void addMember(Long boardId, AddBoardMemberDto addBoardMemberDto) {
    User targetUser = userRepository.findByEmail(addBoardMemberDto.getEmail())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    if(boardMemberRepository.existsByBoardIdAndUserId(boardId, targetUser.getId())){
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "User is already a member of this board"
      );
    }

    BoardRole defaultBoardRole = boardRoleService.findByNameOrThrow("member");

    Board targetBoard = boardRepository.findById(boardId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));;

    BoardMember boardMember = boardMemberMapper.toEntity(targetUser, defaultBoardRole, targetBoard);

    try {
      boardMemberRepository.save(boardMember);
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "User is already a member of this board"
      );
    }
  }
}

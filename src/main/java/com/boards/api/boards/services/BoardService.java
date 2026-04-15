package com.boards.api.boards.services;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.boards.api.authorization.entities.BoardRole;
import com.boards.api.authorization.services.BoardRoleService;
import com.boards.api.boards.dtos.BoardPermissionsResponseDto;
import com.boards.api.boards.dtos.BoardResponseDto;
import com.boards.api.boards.dtos.CreateBoardDto;
import com.boards.api.boards.dtos.UpdateBoardDto;
import com.boards.api.boards.entities.Board;
import com.boards.api.boards.entities.BoardMember;
import com.boards.api.boards.mappers.BoardMapper;
import com.boards.api.boards.mappers.BoardMemberMapper;
import com.boards.api.boards.repositories.BoardMemberRepository;
import com.boards.api.boards.repositories.BoardRepository;
import com.boards.api.common.exceptions.BoardNotFoundException;
import com.boards.api.users.entities.User;
import com.boards.api.users.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;
  private final UserRepository userRepository;
  private final BoardMemberRepository boardMemberRepository;

  private final BoardRoleService boardRoleService;

  private final BoardMemberMapper boardMemberMapper;
  private final BoardMapper boardMapper;

  @Transactional
  public BoardResponseDto create(Long ownerId, CreateBoardDto createBoardDto) {
    User owner = userRepository.findById(ownerId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Owner user not found"
      )
    );

    Board board = boardMapper.toEntity(createBoardDto);
    board.setOwner(owner);

    Board savedBoard = boardRepository.save(board);

    // Adding owner as admin of the created board
    BoardRole adminBoardRole = boardRoleService.findByNameOrThrow("admin");

    BoardMember boardMember = boardMemberMapper.toEntity(owner, adminBoardRole, savedBoard);
    boardMemberRepository.save(boardMember);

    return boardMapper.toResponseDto(savedBoard, true);
  }

  public List<BoardResponseDto> findMyBoards(Long currentUserId) {
    List<Board> myBoards = boardMemberRepository.findBoardsByUserId(currentUserId);
    return myBoards.stream()
      .map(board -> toBoardResponseDto(board, currentUserId))
      .toList();
  }

  public BoardResponseDto findOne(Long boardId, Long userId) {
    Board board = boardRepository.findById(boardId)
      .orElseThrow(BoardNotFoundException::new);

    return toBoardResponseDto(board, userId);
  }

  public BoardPermissionsResponseDto findMyBoardPermissions(Long boardId, Long currentUserId) {
    BoardMember boardMember = boardMemberRepository.findPermissionsByBoardIdAndUserId(boardId, currentUserId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Board membership not found"
      )
    );
    BoardPermissionsResponseDto response = boardMapper.toBoardPermissionsResponseDto(
      boardMember,
      boardMember.getBoard().getOwner().getId().equals(currentUserId)
    );

    response.setPermissions(boardMember.getBoardRole().getPermissions()
      .stream()
      .map(p -> p.getName())
      .collect(Collectors.toSet())
    );

    return response;
  }

  public BoardResponseDto update(Long currentUserId, Long boardId, UpdateBoardDto updateBoardDto) {
    Board board = boardRepository.findById(boardId)
      .orElseThrow(BoardNotFoundException::new);

    board.setName(updateBoardDto.getName());

    Board updatedBoard = boardRepository.save(board);

    return toBoardResponseDto(updatedBoard, currentUserId);
  }

  public void remove(Long boardId) {
    Board board = boardRepository.findById(boardId)
      .orElseThrow(BoardNotFoundException::new);

    boardRepository.delete(board);
  }

  @Transactional
  public void transferOwnership(Long boardId, Long targetUserId) {
    Board targetBoard = boardRepository.findById(boardId)
      .orElseThrow(BoardNotFoundException::new);

    if (targetBoard.getOwner().getId().equals(targetUserId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Target user is already the owner");
    }

    BoardMember targetUserMembership = boardMemberRepository.findDetailedByBoardIdAndUserId(boardId, targetUserId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Target user is not a member of this board"
      )
    );

    if (!targetUserMembership.getBoardRole().getName().equals("admin")) {
      BoardRole adminBoardRole = boardRoleService.findByNameOrThrow("admin");

      targetUserMembership.setBoardRole(adminBoardRole);
      boardMemberRepository.save(targetUserMembership);
    }    
    // Change board owner and let previous owner as admin
    targetBoard.setOwner(targetUserMembership.getUser());
    boardRepository.save(targetBoard);
  }

  private BoardResponseDto toBoardResponseDto(Board board, Long currentUserId) {
    return boardMapper.toResponseDto(
      board,
      board.getOwner().getId().equals(currentUserId)
    );
  }

}

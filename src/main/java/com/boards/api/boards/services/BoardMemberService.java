package com.boards.api.boards.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.boards.api.authorization.entities.BoardRole;
import com.boards.api.authorization.services.BoardRoleService;
import com.boards.api.boards.dtos.AddBoardMemberDto;
import com.boards.api.boards.dtos.BoardMemberResponseDto;
import com.boards.api.boards.dtos.UpdateBoardRoleDto;
import com.boards.api.boards.entities.Board;
import com.boards.api.boards.entities.BoardMember;
import com.boards.api.boards.mappers.BoardMemberMapper;
import com.boards.api.boards.repositories.BoardMemberRepository;
import com.boards.api.boards.repositories.BoardRepository;
import com.boards.api.common.exceptions.BoardNotFoundException;
import com.boards.api.common.exceptions.UserNotFoundException;
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

  @Transactional
  public void addMember(Long boardId, AddBoardMemberDto addBoardMemberDto) {
    User targetUser = userRepository.findByEmail(addBoardMemberDto.getEmail())
      .orElseThrow(UserNotFoundException::new);

    if(boardMemberRepository.existsByBoardIdAndUserId(boardId, targetUser.getId())){
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "User is already a member of this board"
      );
    }

    BoardRole defaultBoardRole = boardRoleService.findByNameOrThrow("member");

    Board targetBoard = boardRepository.findById(boardId)
      .orElseThrow(BoardNotFoundException::new);

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

  @Transactional
  public void removeMember(Long boardId, Long targetUserId, Long currentUserId) {
    BoardMember targetBoardMember = boardMemberRepository.findByBoardIdAndUserId(boardId, targetUserId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Target user is not a member of this board"
      )
    );

    if (targetBoardMember.getBoard().getOwner().getId().equals(targetUserId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot remove the owner");
    }

    boolean currentUserIsOwner = targetBoardMember.getBoard().getOwner().getId().equals(currentUserId);

    if (targetBoardMember.getBoardRole().getName().equals("admin") && !currentUserIsOwner) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot remove another admin");
    }

    boardMemberRepository.delete(targetBoardMember);
  }

  public List<BoardMemberResponseDto> findBoardMembers(Long boardId) {
    List<BoardMember> boardMembers = boardMemberRepository.findByBoardId(boardId);

    Long boardOwnerId = boardMembers.isEmpty()
      ? null
      : boardMembers.get(0).getBoard().getOwner().getId();

    List<BoardMemberResponseDto> response = boardMembers
      .stream()
      .map(
        bm -> boardMemberMapper.toBoardMemberResponseDto(bm, boardOwnerId == bm.getUser().getId())
      ).toList();

    return response;
  }

  @Transactional
  public void updateBoardRole(Long boardId, Long targetUserId, UpdateBoardRoleDto updateBoardRoleDto, Long currentUserId) {    
    if (targetUserId.equals(currentUserId)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot change own board role");

    BoardMember targetBoardMember = boardMemberRepository.findByBoardIdAndUserId(boardId, targetUserId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Target user does not belong to this board."
      )
    );

    String targetRoleName = updateBoardRoleDto.getRole();

    if (targetRoleName.equals(targetBoardMember.getBoardRole().getName())) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Target user already has the board role"
      );
    }

    BoardRole targetBoardRole = boardRoleService.findByNameOrThrow(targetRoleName);

    boolean currentUserIsOwner = targetBoardMember.getBoard().getOwner().getId().equals(currentUserId);
    boolean targetUserIsAdmin = targetBoardMember.getBoardRole().getName().equals("admin");
    boolean newRoleIsMember = targetRoleName.equals("member");

    // Admin tries to degrade another admin
    if (!currentUserIsOwner && targetUserIsAdmin && newRoleIsMember) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin cannot change another admin role");
    }

    targetBoardMember.setBoardRole(targetBoardRole);
    boardMemberRepository.save(targetBoardMember);
  }
}

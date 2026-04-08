package com.boards.api.authorization.services;

import org.springframework.stereotype.Service;

import com.boards.api.boards.repositories.BoardMemberRepository;
import com.boards.api.boards.repositories.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardAuthorizationService {
  private final BoardMemberRepository boardMemberRepository;
  private final BoardRepository boardRepository;

  public boolean hasBoardPermission(Long userId, Long boardId, String permissionName) {
    return boardMemberRepository.findDetailedByBoardIdAndUserId(boardId, userId)
      .map( // if belongs to board check permission
        boardMember -> boardMember.getBoardRole().getPermissions().stream()
        .anyMatch(
          permission -> permission.getName().equals(permissionName)
        )
      )
      .orElse(false);
  }

  public boolean isBoardOwner(Long userId, Long boardId) {
    return boardRepository.existsByIdAndOwnerId(boardId, userId);
  }
}

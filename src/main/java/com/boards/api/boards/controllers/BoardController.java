package com.boards.api.boards.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.boards.api.boards.dtos.BoardPermissionsResponseDto;
import com.boards.api.boards.dtos.BoardResponseDto;
import com.boards.api.boards.dtos.CreateBoardDto;
import com.boards.api.boards.dtos.UpdateBoardDto;
import com.boards.api.boards.services.BoardService;
import com.boards.api.security.AuthenticatedUser;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@Tag(name = "Boards")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {  
  private final BoardService boardService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAuthority('board_create')")
  public BoardResponseDto create(
    @Valid @RequestBody CreateBoardDto createBoardDto,
    @AuthenticationPrincipal AuthenticatedUser currentUser
  ) {
    return boardService.create(currentUser.getId(), createBoardDto);
  }
  
  @GetMapping("/me")
  @PreAuthorize("hasAuthority('board_read')")
  public List<BoardResponseDto> findMyBoards(@AuthenticationPrincipal AuthenticatedUser currentUser) {
    return boardService.findMyBoards(currentUser.getId());
  }
  
  @GetMapping("/{boardId}")
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'board_read_full_board')")
  public BoardResponseDto findOne(
    @PathVariable Long boardId,
    @AuthenticationPrincipal AuthenticatedUser currentUser
  ) {
    return boardService.findOne(boardId, currentUser.getId());
  }

  @GetMapping("/{boardId}/my-permissions")
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'board_read_full_board')")
  public BoardPermissionsResponseDto findMyBoardPermissions(
    @AuthenticationPrincipal AuthenticatedUser currentUser,
    @PathVariable Long boardId
  ) {
    return boardService.findMyBoardPermissions(boardId, currentUser.getId());
  }

  @PutMapping("/{boardId}")
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'board_update')")
  public BoardResponseDto update(
    @PathVariable Long boardId,
    @RequestBody UpdateBoardDto updateBoardDto,
    @AuthenticationPrincipal AuthenticatedUser currentUser
  ) {
    return boardService.update(currentUser.getId(), boardId, updateBoardDto);
  }

  @DeleteMapping("/{boardId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("@boardAuthorizationService.isBoardOwner(authentication.principal.id, #boardId)")
  public void remove(@PathVariable Long boardId){
    boardService.remove(boardId);
  }
  
  @PutMapping("/{boardId}/transfer-ownership/{targetUserId}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("@boardAuthorizationService.isBoardOwner(authentication.principal.id, #boardId)")
  public void transferOwnership(
    @PathVariable Long boardId,
    @PathVariable Long targetUserId
  ) {
    boardService.transferOwnership(boardId, targetUserId);
  }
}

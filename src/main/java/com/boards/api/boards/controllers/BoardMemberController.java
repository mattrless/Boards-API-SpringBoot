package com.boards.api.boards.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.boards.api.boards.dtos.AddBoardMemberDto;
import com.boards.api.boards.services.BoardMemberService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Tag(name = "Board Members")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardMemberController {
  private final BoardMemberService boardMemberService;
  
  @PostMapping("/{boardId}/add-member")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'board_add_members')")
  public void addMember(
    @PathVariable Long boardId,
    @Valid @RequestBody AddBoardMemberDto addBoardMemberDto
  ) {
    boardMemberService.addMember(boardId, addBoardMemberDto);
  }
}

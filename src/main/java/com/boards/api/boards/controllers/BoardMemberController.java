package com.boards.api.boards.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.boards.api.boards.docs.boardmember.AddBoardMemberDocs;
import com.boards.api.boards.docs.boardmember.FindBoardMembersDocs;
import com.boards.api.boards.docs.boardmember.RemoveBoardMemberDocs;
import com.boards.api.boards.docs.boardmember.UpdateBoardMemberRoleDocs;
import com.boards.api.boards.dtos.AddBoardMemberDto;
import com.boards.api.boards.dtos.BoardMemberResponseDto;
import com.boards.api.boards.dtos.UpdateBoardRoleDto;
import com.boards.api.boards.services.BoardMemberService;
import com.boards.api.security.AuthenticatedUser;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Board Members")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardMemberController {
  private final BoardMemberService boardMemberService;
  
  @PostMapping("/{boardId}/add-member")
  @AddBoardMemberDocs
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'board_add_members')")
  public void addMember(
    @PathVariable Long boardId,
    @Valid @RequestBody AddBoardMemberDto addBoardMemberDto
  ) {
    boardMemberService.addMember(boardId, addBoardMemberDto);
  }

  @DeleteMapping("/{boardId}/remove-member/{targetUserId}")
  @RemoveBoardMemberDocs
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'board_remove_members')")
  public void removeMember(    
    @PathVariable Long boardId,
    @PathVariable Long targetUserId,
    @AuthenticationPrincipal AuthenticatedUser currentUser
  ) {
    boardMemberService.removeMember(boardId, targetUserId, currentUser.getId());
  }

  @GetMapping("/{boardId}/members")
  @FindBoardMembersDocs
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'board_view_members')")
  public List<BoardMemberResponseDto> findBoardMembers(
    @PathVariable Long boardId
  ) {
    return boardMemberService.findBoardMembers(boardId);
  }
  
  @PutMapping("/{boardId}/members/{targetUserId}/role")
  @UpdateBoardMemberRoleDocs
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'board_update_member_role')")  
  public void updateBoardRole(
    @PathVariable Long boardId,
    @PathVariable Long targetUserId,
    @Valid @RequestBody UpdateBoardRoleDto updateBoardRoleDto,
    @AuthenticationPrincipal AuthenticatedUser currentUser
  ) {
    boardMemberService.updateBoardRole(boardId, targetUserId, updateBoardRoleDto, currentUser.getId());
  }
}

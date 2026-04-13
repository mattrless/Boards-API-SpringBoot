package com.boards.api.cards.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.boards.api.cards.docs.cardassignments.CreateCardAssignmentDocs;
import com.boards.api.cards.docs.cardassignments.DeleteCardAssignmentDocs;
import com.boards.api.cards.docs.cardassignments.FindCardAssigmentsDocs;
import com.boards.api.cards.dtos.CreateCardAssignmentDto;
import com.boards.api.cards.services.CardAssignmentService;
import com.boards.api.security.AuthenticatedUser;
import com.boards.api.users.dtos.UserResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Card Members")
@RestController
@RequestMapping("/boards/{boardId}/cards/{cardId}/members")
@RequiredArgsConstructor
public class CardAssignmentController {
  private final CardAssignmentService cardAssignmentService;

  @PostMapping
  @CreateCardAssignmentDocs
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'card_update')")
  public void addMember(
    @PathVariable Long boardId,    
    @PathVariable Long cardId,
    @Valid @RequestBody CreateCardAssignmentDto createCardAssignmentDto,
    @AuthenticationPrincipal AuthenticatedUser currentUser
  ) {
    cardAssignmentService.addMember(boardId, cardId, createCardAssignmentDto, currentUser.getId());
  }

  @GetMapping
  @FindCardAssigmentsDocs
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'card_read')")
  public List<UserResponseDto> findAll(
    @PathVariable Long boardId,
    @PathVariable Long cardId
  ) {    
    return cardAssignmentService.findAll(boardId, cardId);
  }
  
  @DeleteMapping("/{targetMemberId}")
  @DeleteCardAssignmentDocs
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'card_update')")
  public void deleteMember(
    @PathVariable Long boardId,
    @PathVariable Long cardId,
    @AuthenticationPrincipal AuthenticatedUser currentUser,
    @PathVariable Long targetMemberId
  ) {
    cardAssignmentService.deleteMember(boardId, cardId, currentUser.getId(), targetMemberId);
  }
}

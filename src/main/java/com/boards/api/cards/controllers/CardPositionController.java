package com.boards.api.cards.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boards.api.cards.docs.cards.UpdateCardPositionDocs;
import com.boards.api.cards.dtos.CardPositionUpdatedResponseDto;
import com.boards.api.cards.dtos.UpdateCardPositionDto;
import com.boards.api.cards.services.CardService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cards")
@RestController
@RequestMapping("/boards/{boardId}/cards")
@RequiredArgsConstructor
public class CardPositionController {
  private final CardService cardService;

  @PutMapping("/{cardId}/position")
  @UpdateCardPositionDocs
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'card_update')")
  public CardPositionUpdatedResponseDto updatePosition(
    @PathVariable Long boardId,
    @PathVariable Long cardId,
    @Valid @RequestBody UpdateCardPositionDto updateCardPositionDto
  ) {
    return cardService.updatePosition(boardId, cardId, updateCardPositionDto);
  }
}

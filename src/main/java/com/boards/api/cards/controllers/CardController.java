package com.boards.api.cards.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.boards.api.cards.docs.cards.CreateCardDocs;
import com.boards.api.cards.docs.cards.FindAllCardsDocs;
import com.boards.api.cards.docs.cards.FindCardByIdDocs;
import com.boards.api.cards.docs.cards.RemoveCardDocs;
import com.boards.api.cards.docs.cards.UpdateCardDocs;
import com.boards.api.cards.dtos.CardResponseDto;
import com.boards.api.cards.dtos.CreateCardDto;
import com.boards.api.cards.dtos.UpdateCardDto;
import com.boards.api.cards.services.CardService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Cards")
@RestController
@RequestMapping("/boards/{boardId}/lists/{boardListId}/cards")
@RequiredArgsConstructor
public class CardController {
  private final CardService cardService;

  @PostMapping
  @CreateCardDocs
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'card_create')")
  public CardResponseDto create(
    @Valid @RequestBody CreateCardDto createCardDto,
    @PathVariable Long boardId,
    @PathVariable Long boardListId
  ) {
    return cardService.create(createCardDto, boardId, boardListId);
  }

  @GetMapping
  @FindAllCardsDocs
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'card_read')")
  public List<CardResponseDto> findAll(
    @PathVariable Long boardId,
    @PathVariable Long boardListId
  ) {
    return cardService.findAll(boardId, boardListId);
  }
  
  @GetMapping("/{cardId}")
  @FindCardByIdDocs
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'card_read')")
  public CardResponseDto findOne(
    @PathVariable Long boardId,
    @PathVariable Long boardListId,
    @PathVariable Long cardId
  ) {
    return cardService.findOne(boardId, boardListId, cardId);
  }
  
  @PutMapping("/{cardId}")
  @UpdateCardDocs
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'card_update')")
  public CardResponseDto update(
    @PathVariable Long boardId,
    @PathVariable Long boardListId,
    @PathVariable Long cardId,
    @Valid @RequestBody UpdateCardDto updateCardDto
  ) {
    return cardService.update(boardId, boardListId, cardId, updateCardDto);
  }

  @DeleteMapping("/{cardId}")
  @RemoveCardDocs
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'card_delete')")
  public void delete(
    @PathVariable Long boardId,
    @PathVariable Long boardListId,
    @PathVariable Long cardId
  ) {
    cardService.delete(boardId, boardListId, cardId);
  }
}

package com.boards.api.cards.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.boards.api.boardlists.entities.BoardList;
import com.boards.api.boardlists.services.BoardListService;
import com.boards.api.cards.dtos.CardResponseDto;
import com.boards.api.cards.dtos.CreateCardDto;
import com.boards.api.cards.dtos.UpdateCardDto;
import com.boards.api.cards.entities.Card;
import com.boards.api.cards.mappers.CardMapper;
import com.boards.api.cards.repositories.CardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {
  private final CardRepository cardRepository;
  private final CardMapper cardMapper;
  private final BoardListService boardListService;

  private static final BigDecimal GAP = BigDecimal.valueOf(1000);
  // private static final BigDecimal MIN_POSITION_GAP = BigDecimal.ONE;
  // private static final BigDecimal DIVISOR_TWO = BigDecimal.valueOf(2);

  @Transactional
  public CardResponseDto create(CreateCardDto createCardDto, Long boardId, Long boardListId) {
    BoardList boardList = boardListService.findByIdAndBoardIdOrThrow(boardListId, boardId);
    Card card = cardMapper.toEntity(createCardDto);

    BigDecimal newPosition = cardRepository.findTopByBoardList_IdOrderByPositionDesc(boardListId)
      .map(c -> c.getPosition().add(GAP))
      .orElse(BigDecimal.ZERO);

    card.setPosition(newPosition);
    card.setBoardList(boardList);

    Card savedCard = cardRepository.save(card);
    return cardMapper.toResponseDto(savedCard);
  }

  public List<CardResponseDto> findAll(Long boardId, Long boardListId) {
    // Validate the list belongs to the board
    boardListService.findByIdAndBoardIdOrThrow(boardListId, boardId);

    List<Card> cards = cardRepository.findByBoardList_IdOrderByPositionAsc(boardListId);

    return cards.stream()
      .map(c -> cardMapper.toResponseDto(c))
      .toList();
  }
  
  public CardResponseDto findOne(Long boardId, Long boardListId, Long cardId) {
    Card card = findByIdAndBoardListIdOrThrow(cardId, boardListId, boardId);
    return cardMapper.toResponseDto(card);
  }

  @Transactional
  public CardResponseDto update(Long boardId, Long boardListId, Long cardId, UpdateCardDto updateCardDto) {
    Card card = findByIdAndBoardListIdOrThrow(cardId, boardListId, boardId);

    cardMapper.updateEntity(updateCardDto, card);

    return cardMapper.toResponseDto(cardRepository.save(card));
  }

  @Transactional
  public void delete(Long boardId, Long boardListId, Long cardId) {
    Card card = findByIdAndBoardListIdOrThrow(cardId, boardListId, boardId);
    cardRepository.delete(card);
  }

  private Card findByIdAndBoardListIdOrThrow(Long cardId, Long boardListId, Long boardId) {
    // Validate list belongs to board and card belongs to list
    boardListService.findByIdAndBoardIdOrThrow(boardListId, boardId);
    return cardRepository.findByIdAndBoardList_Id(cardId, boardListId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Card not found"
      )
    );
  }

}

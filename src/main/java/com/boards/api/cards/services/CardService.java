package com.boards.api.cards.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.boards.api.boardlists.entities.BoardList;
import com.boards.api.boardlists.services.BoardListService;
import com.boards.api.cards.dtos.CardPositionUpdatedResponseDto;
import com.boards.api.cards.dtos.CardResponseDto;
import com.boards.api.cards.dtos.CreateCardDto;
import com.boards.api.cards.dtos.UpdateCardDto;
import com.boards.api.cards.dtos.UpdateCardPositionDto;
import com.boards.api.cards.entities.Card;
import com.boards.api.cards.mappers.CardMapper;
import com.boards.api.cards.repositories.CardRepository;
import com.boards.api.boards.entities.BoardMember;
import com.boards.api.boards.repositories.BoardMemberRepository;
import com.boards.api.boards.repositories.BoardRepository;
import com.boards.api.common.exceptions.BoardNotFoundException;
import com.boards.api.common.exceptions.CardNotFoundException;
import com.boards.api.websockets.events.CardWsEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {
  private final CardRepository cardRepository;
  private final CardMapper cardMapper;
  private final BoardListService boardListService;
  private final BoardRepository boardRepository;
  private final BoardMemberRepository boardMemberRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  private static final BigDecimal GAP = BigDecimal.valueOf(1000);
  private static final BigDecimal MIN_POSITION_GAP = BigDecimal.ONE;
  private static final BigDecimal DIVISOR_TWO = BigDecimal.valueOf(2);

  @Transactional
  public CardResponseDto create(CreateCardDto createCardDto, Long boardId, Long boardListId) {
    boardRepository.findWithLockById(boardId)
      .orElseThrow(BoardNotFoundException::new);

    BoardList boardList = boardListService.findByIdAndBoardIdOrThrow(boardListId, boardId);
    Card card = cardMapper.toEntity(createCardDto);

    BigDecimal newPosition = cardRepository.findTopByBoardList_IdOrderByPositionDesc(boardListId)
      .map(c -> c.getPosition().add(GAP))
      .orElse(BigDecimal.ZERO);

    card.setPosition(newPosition);
    card.setBoardList(boardList);

    Card savedCard = cardRepository.save(card);

    List<BoardMember> boardMembers = boardMemberRepository.findByBoardId(boardId);
    applicationEventPublisher.publishEvent(
      new CardWsEvent("card:created", boardMembers, boardId, savedCard.getId(), boardList.getId(), null, null)
    );

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

    Card updatedCard = cardRepository.save(card);

    List<BoardMember> boardMembers = boardMemberRepository.findByBoardId(boardId);
    applicationEventPublisher.publishEvent(
      new CardWsEvent("card:updated", boardMembers, boardId, updatedCard.getId(), null, null, null)
    );

    return cardMapper.toResponseDto(updatedCard);
  }

  @Transactional
  public void delete(Long boardId, Long boardListId, Long cardId) {
    Card card = findByIdAndBoardListIdOrThrow(cardId, boardListId, boardId);

    List<BoardMember> boardMembers = boardMemberRepository.findByBoardId(boardId);    

    cardRepository.delete(card);

    applicationEventPublisher.publishEvent(
      new CardWsEvent("card:removed", boardMembers, boardId, cardId, null, null, null)
    );
  }

  @Transactional
  public CardPositionUpdatedResponseDto updatePosition(Long boardId, Long cardId, UpdateCardPositionDto updateCardPositionDto, Long currentUserId) {
    boardRepository.findWithLockById(boardId)
      .orElseThrow(BoardNotFoundException::new);

    Card targetCard = cardRepository.findByIdAndBoardList_Board_Id(cardId, boardId)
      .orElseThrow(CardNotFoundException::new);
    
    Long sourceBoardListId = targetCard.getBoardList().getId();

    Long prevCardId = updateCardPositionDto.getPrevCardId();
    Long nextCardId = updateCardPositionDto.getNextCardId();
    Long targetBoardListId = updateCardPositionDto.getTargetBoardListId();
    // if the body has just targetBoardListId then we try to move to empty list
    boolean placeInEmptyBoardList = prevCardId == null && nextCardId == null && targetBoardListId != null;
    boolean movingToAnotherList = targetBoardListId != null && (prevCardId != null || nextCardId != null);

    BigDecimal newPosition = BigDecimal.ZERO;

    if (placeInEmptyBoardList) {
      validatePositionInEmptyList(boardId, targetBoardListId);
    } else if (!movingToAnotherList){
      targetBoardListId = sourceBoardListId;
    }

    validateReferences(boardId, cardId, sourceBoardListId, prevCardId, nextCardId, targetBoardListId);

    boolean placeBefore = nextCardId != null;
    boolean placeAfter = prevCardId != null;
    boolean inBetween = placeAfter && placeBefore;    

    if (inBetween) {
      newPosition = calculateInBetweenPosition(boardId, prevCardId, nextCardId, targetBoardListId);
    } else if (placeAfter) {
      newPosition = calculateAfterPosition(boardId, prevCardId, targetBoardListId);
    } else if (placeBefore) {
      newPosition = calculateBeforePosition(boardId, nextCardId, targetBoardListId);
    }

    BoardList targetBoardList = boardListService.findByIdAndBoardIdOrThrow(targetBoardListId, boardId);

    targetCard.setBoardList(targetBoardList);
    targetCard.setPosition(newPosition);

    Card updatedCard = cardRepository.save(targetCard);

    List<BoardMember> boardMembers = boardMemberRepository.findByBoardId(boardId);
    applicationEventPublisher.publishEvent(
      new CardWsEvent("card:moved", boardMembers, boardId, updatedCard.getId(), targetBoardList.getId(), sourceBoardListId, currentUserId)
    );

    return cardMapper.toUpdatedPositionResponseDto(updatedCard, targetBoardList, sourceBoardListId);
  }

  private void validatePositionInEmptyList(Long boardId, Long targetBoardListId) {
    Long cardsNumber = cardRepository.countByBoardList_IdAndBoardList_Board_Id(targetBoardListId, boardId);

    if(cardsNumber != 0) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Need prev or next card id to place in a no empty list"
      );
    }
  }
  private BigDecimal calculateBeforePosition(Long boardId, Long nextCardId, Long targetBoardListId) {
    Card nextCard = findByIdAndBoardListIdOrThrow(nextCardId, targetBoardListId, boardId);
    BigDecimal nextCardPosition = nextCard.getPosition();

    return nextCardPosition.subtract(GAP);
  }

  private BigDecimal calculateAfterPosition(Long boardId, Long prevCardId, Long targetBoardListId) {
    Card prevCard = findByIdAndBoardListIdOrThrow(prevCardId, targetBoardListId, boardId);
    BigDecimal prevCardPosition = prevCard.getPosition();

    return prevCardPosition.add(GAP);
  }

  private BigDecimal calculateInBetweenPosition(Long boardId, Long prevCardId, Long nextCardId, Long targetBoardListId) {
    Card prevCard = findByIdAndBoardListIdOrThrow(prevCardId, targetBoardListId, boardId);
    Card nextCard = findByIdAndBoardListIdOrThrow(nextCardId, targetBoardListId, boardId);

    BigDecimal prevCardPosition = prevCard.getPosition();
    BigDecimal nextCardPosition = nextCard.getPosition();

    if (nextCardPosition.compareTo(prevCardPosition) < 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Next card position should be greater than prev card position");
    }

    BigDecimal positionGap = nextCardPosition.subtract(prevCardPosition);

    // Rebalance in case we run out of space between cards
    if (positionGap.compareTo(MIN_POSITION_GAP) <= 0) {
      rebalanceCards(targetBoardListId);

      prevCard = findByIdAndBoardListIdOrThrow(prevCardId, targetBoardListId, boardId);
      nextCard = findByIdAndBoardListIdOrThrow(nextCardId, targetBoardListId, boardId);

      prevCardPosition = prevCard.getPosition();
      nextCardPosition = nextCard.getPosition();
    }

    return prevCardPosition.add(nextCardPosition).divide(DIVISOR_TWO, 2, RoundingMode.HALF_UP);
  }

  private void rebalanceCards(Long boardListId) {
    List<Card> cards = cardRepository.findByBoardList_IdOrderByPositionAsc(boardListId);
    BigDecimal position = GAP;

    for (Card card : cards) {
      card.setPosition(position);
      position = position.add(GAP);
    }

    cardRepository.saveAll(cards);
  }

  private void validateReferences(Long boardId, Long cardId, Long targetCardListId, Long prevCardId, Long nextCardId, Long targetBoardListId) {
    // Validate the list belongs to the board
    boardListService.findByIdAndBoardIdOrThrow(targetCardListId, boardId);
    if (!targetCardListId.equals(targetBoardListId)) {
      boardListService.findByIdAndBoardIdOrThrow(targetBoardListId, boardId);
    }

    if ((prevCardId != null && prevCardId.equals(cardId)) || (nextCardId != null && nextCardId.equals(cardId))) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot position a card relative to itself");
    }

    if (prevCardId != null && nextCardId != null && prevCardId.equals(nextCardId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "prevCardId must be different from nextCardId");
    }
  }

  private Card findByIdAndBoardListIdOrThrow(Long cardId, Long boardListId, Long boardId) {
    // Validate list belongs to board and card belongs to list
    boardListService.findByIdAndBoardIdOrThrow(boardListId, boardId);
    return cardRepository.findByIdAndBoardList_Id(cardId, boardListId)
      .orElseThrow(CardNotFoundException::new);
  }
}

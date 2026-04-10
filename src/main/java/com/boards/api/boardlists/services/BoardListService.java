package com.boards.api.boardlists.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.boards.api.boardlists.dtos.BoardListResponseDto;
import com.boards.api.boardlists.dtos.CreateBoardListDto;
import com.boards.api.boardlists.dtos.UpdateBoardListDto;
import com.boards.api.boardlists.dtos.UpdateBoardListPositionDto;
import com.boards.api.boardlists.entities.BoardList;
import com.boards.api.boardlists.mappers.BoardListMapper;
import com.boards.api.boardlists.repositories.BoardListRepository;
import com.boards.api.boards.entities.Board;
import com.boards.api.boards.repositories.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardListService {
  private static final BigDecimal GAP = BigDecimal.valueOf(1000);
  private static final BigDecimal MIN_POSITION_GAP = BigDecimal.ONE;
  private static final BigDecimal DIVISOR_TWO = BigDecimal.valueOf(2);

  private final BoardListRepository boardListRepository;
  private final BoardRepository boardRepository;
  private final BoardListMapper boardListMapper;

  @Transactional
  public BoardListResponseDto create(Long boardId, CreateBoardListDto createBoardListDto) {
    Board board = boardRepository.findById(boardId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Board not found"
      )
    );

    BigDecimal newPosition = boardListRepository.findTopByBoard_IdOrderByPositionDesc(boardId)
      .map(bl -> bl.getPosition().add(GAP))
      .orElse(BigDecimal.ZERO);

    BoardList boardList = boardListMapper.toEntity(createBoardListDto);
    
    boardList.setPosition(newPosition);
    boardList.setBoard(board);

    BoardList savedBoardList = boardListRepository.save(boardList);

    return boardListMapper.toResponseDto(savedBoardList);
  }

  public List<BoardListResponseDto> findAll(Long boardId) {
    List<BoardList> boardLists = boardListRepository.findByBoard_IdOrderByPositionAsc(boardId);

    return boardLists.stream()
      .map(boardListMapper::toResponseDto)
      .toList();
  }

  public BoardListResponseDto findOne(Long boardId, Long boardListId) {
    BoardList boardList = findByIdAndBoardIdOrThrow(boardListId, boardId);
    return boardListMapper.toResponseDto(boardList);
  }

  @Transactional
  public BoardListResponseDto update(Long boardId, Long boardListId, UpdateBoardListDto updateBoardListDto) {
    BoardList boardList = findByIdAndBoardIdOrThrow(boardListId, boardId);
    boardList.setTitle(updateBoardListDto.getTitle());
    BoardList updatedBoardList = boardListRepository.save(boardList);
    return boardListMapper.toResponseDto(updatedBoardList);
  }

  @Transactional
  public void delete(Long boardId, Long boardListId) {
    BoardList boardList = findByIdAndBoardIdOrThrow(boardListId, boardId);
    boardListRepository.delete(boardList);
  }

  @Transactional
  public BoardListResponseDto updatePosition(Long boardId, Long boardListId, UpdateBoardListPositionDto updateBoardListPositionDto) {
    Long prevBoardListId = updateBoardListPositionDto.getPrevBoardListId();
    Long nextBoardListId = updateBoardListPositionDto.getNextBoardListId();

    validatePositionReferences(boardListId, prevBoardListId, nextBoardListId);

    BoardList boardList = findByIdAndBoardIdOrThrow(boardListId, boardId);    
    
    BigDecimal newPosition = BigDecimal.ZERO;

    boolean placeAfter = prevBoardListId != null; // To place after de list id from body
    boolean placeBefore = nextBoardListId != null; // To place before de list id from body
    boolean inBetween = placeAfter && placeBefore;

    if (inBetween) {
      newPosition = calculateInBetweenPosition(boardId, prevBoardListId, nextBoardListId);
    } else if (placeAfter) {
      newPosition = calculateAfterPosition(boardId, prevBoardListId);
    } else if (placeBefore) {
      newPosition = calculateBeforePosition(boardId, nextBoardListId);
    }

    boardList.setPosition(newPosition);
    BoardList updatedBoardList = boardListRepository.save(boardList);

    return boardListMapper.toResponseDto(updatedBoardList);
  }

  private void rebalanceBoardLists(Long boardId) {
    List<BoardList> boardLists = boardListRepository.findByBoard_IdOrderByPositionAsc(boardId);
    BigDecimal position = GAP;

    for (BoardList boardList : boardLists) {
      boardList.setPosition(position);
      position = position.add(GAP);
    }

    boardListRepository.saveAll(boardLists);
  }

  private void validatePositionReferences(Long boardListId, Long prevBoardListId, Long nextBoardListId) {
    if (prevBoardListId != null && prevBoardListId.equals(nextBoardListId)) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "prevBoardListId must be different from nextBoardListId"
      );
    }

    if (
      (prevBoardListId != null && prevBoardListId.equals(boardListId)) ||
      (nextBoardListId != null && nextBoardListId.equals(boardListId))
    ) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Cannot position a list relative to itself"
      );
    }
  }

  private BigDecimal calculateInBetweenPosition(Long boardId, Long prevBoardListId, Long nextBoardListId) {
    BoardList prevBoardList = findByIdAndBoardIdOrThrow(prevBoardListId, boardId);
    BoardList nextBoardList = findByIdAndBoardIdOrThrow(nextBoardListId, boardId);      

    BigDecimal prevBoardListPosition = prevBoardList.getPosition();
    BigDecimal nextBoardListPosition = nextBoardList.getPosition();

    if (nextBoardListPosition.compareTo(prevBoardListPosition) < 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Next list must be positioned after previous list");
    }

    BigDecimal positionGap = nextBoardListPosition.subtract(prevBoardListPosition);

    // Rebalance in case we run out of space between lists
    if (positionGap.compareTo(MIN_POSITION_GAP) <= 0) {
      rebalanceBoardLists(boardId);

      prevBoardList = findByIdAndBoardIdOrThrow(prevBoardListId, boardId);
      nextBoardList = findByIdAndBoardIdOrThrow(nextBoardListId, boardId);

      prevBoardListPosition = prevBoardList.getPosition();
      nextBoardListPosition = nextBoardList.getPosition();
    }

    return prevBoardListPosition.add(nextBoardListPosition).divide(DIVISOR_TWO, 2, RoundingMode.HALF_UP);
  }

  private BigDecimal calculateAfterPosition(Long boardId, Long prevBoardListId) {
    BoardList prevBoardList = findByIdAndBoardIdOrThrow(prevBoardListId, boardId);
    BigDecimal prevBoardListPosition = prevBoardList.getPosition();

    return prevBoardListPosition.add(GAP);
  }

  private BigDecimal calculateBeforePosition(Long boardId, Long nextBoardListId) {
    BoardList nextBoardList = findByIdAndBoardIdOrThrow(nextBoardListId, boardId);
    BigDecimal nextBoardListPosition = nextBoardList.getPosition();

    return nextBoardListPosition.subtract(GAP);
  }

  public BoardList findByIdAndBoardIdOrThrow(Long boardListId, Long boardId) {
    return boardListRepository.findByIdAndBoard_Id(boardListId, boardId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "List not found"
      )
    );
  }
}

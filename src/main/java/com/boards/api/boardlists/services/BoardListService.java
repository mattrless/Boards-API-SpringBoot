package com.boards.api.boardlists.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.boards.api.boardlists.dtos.BoardListResponseDto;
import com.boards.api.boardlists.dtos.CreateBoardListDto;
import com.boards.api.boardlists.entities.BoardList;
import com.boards.api.boardlists.mappers.BoardListMapper;
import com.boards.api.boardlists.repositories.BoardListRepository;
import com.boards.api.boards.entities.Board;
import com.boards.api.boards.repositories.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardListService {
  private final BoardListRepository boardListRepository;
  private final BoardRepository boardRepository;
  private final BoardListMapper boardListMapper;

  private static final BigDecimal GAP = BigDecimal.valueOf(1000);

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
      .map(
        bl -> boardListMapper.toResponseDto(bl)
      ).toList();
  }
}

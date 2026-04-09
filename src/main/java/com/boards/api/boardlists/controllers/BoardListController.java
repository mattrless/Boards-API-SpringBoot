package com.boards.api.boardlists.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.boards.api.boardlists.dtos.BoardListResponseDto;
import com.boards.api.boardlists.dtos.CreateBoardListDto;
import com.boards.api.boardlists.services.BoardListService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/boards/{boardId}/lists")
@RequiredArgsConstructor
public class BoardListController {
  private final BoardListService boardListService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'list_create')")
  public BoardListResponseDto create(
    @PathVariable Long boardId,
    @Valid @RequestBody CreateBoardListDto createListDto
  ) {            
    return boardListService.create(boardId, createListDto);
  }
  
  @GetMapping
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'list_read')")
  public List<BoardListResponseDto> findAll(
    @PathVariable Long boardId
  ) {
    return boardListService.findAll(boardId);
  }
  
}

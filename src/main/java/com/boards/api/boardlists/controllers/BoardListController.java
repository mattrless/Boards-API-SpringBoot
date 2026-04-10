package com.boards.api.boardlists.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.boards.api.boardlists.docs.CreateBoardListDocs;
import com.boards.api.boardlists.docs.FindAllBoardListsDocs;
import com.boards.api.boardlists.docs.FindBoardListByIdDocs;
import com.boards.api.boardlists.docs.RemoveBoardListDocs;
import com.boards.api.boardlists.docs.UpdateBoardListDocs;
import com.boards.api.boardlists.docs.UpdateBoardListPositionDocs;
import com.boards.api.boardlists.dtos.BoardListResponseDto;
import com.boards.api.boardlists.dtos.CreateBoardListDto;
import com.boards.api.boardlists.dtos.UpdateBoardListDto;
import com.boards.api.boardlists.dtos.UpdateBoardListPositionDto;
import com.boards.api.boardlists.services.BoardListService;

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

@Tag(name = "Board Lists")
@RestController
@RequestMapping("/boards/{boardId}/lists")
@RequiredArgsConstructor
public class BoardListController {
  private final BoardListService boardListService;

  @PostMapping
  @CreateBoardListDocs
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'list_create')")
  public BoardListResponseDto create(
    @PathVariable Long boardId,
    @Valid @RequestBody CreateBoardListDto createListDto
  ) {            
    return boardListService.create(boardId, createListDto);
  }
  
  @GetMapping
  @FindAllBoardListsDocs
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'list_read')")
  public List<BoardListResponseDto> findAll(
    @PathVariable Long boardId
  ) {
    return boardListService.findAll(boardId);
  }
  
  @GetMapping("/{boardListId}")
  @FindBoardListByIdDocs
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'list_read')")
  public BoardListResponseDto findOne(
    @PathVariable Long boardId,
    @PathVariable Long boardListId
  ) {
    return boardListService.findOne(boardId, boardListId);
  }
  
  @PutMapping("/{boardListId}")
  @UpdateBoardListDocs
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'list_update')")
  public BoardListResponseDto update(
    @PathVariable Long boardId,
    @PathVariable Long boardListId,
    @Valid @RequestBody UpdateBoardListDto updateBoardListDto
  ) {
    return  boardListService.update(boardId, boardListId, updateBoardListDto);
  }

  @DeleteMapping("/{boardListId}")
  @RemoveBoardListDocs
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'list_delete')")
  public void delete(
    @PathVariable Long boardId,
    @PathVariable Long boardListId
  ) {
    boardListService.delete(boardId, boardListId);
  }

  @PutMapping("/{boardListId}/position")
  @UpdateBoardListPositionDocs
  @PreAuthorize("@boardAuthorizationService.hasBoardPermission(authentication.principal.id, #boardId, 'list_update')")
  public BoardListResponseDto updatePosition(
    @PathVariable Long boardId,
    @PathVariable Long boardListId,
    @Valid @RequestBody UpdateBoardListPositionDto updateBoardListPositionDto
  ) {
    return boardListService.updatePosition(boardId, boardListId, updateBoardListPositionDto);
  }
}

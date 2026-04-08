package com.boards.api.authorization.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.boards.api.authorization.entities.BoardRole;
import com.boards.api.authorization.repositories.BoardRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardRoleService {
  private final BoardRoleRepository boardRoleRepository;

  public BoardRole findByNameOrThrow(String name) {
    return boardRoleRepository.findByName(name)
      .orElseThrow(() -> new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Board role not found"
      )
    );
  }
}

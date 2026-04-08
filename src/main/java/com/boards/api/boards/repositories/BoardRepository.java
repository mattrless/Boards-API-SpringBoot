package com.boards.api.boards.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boards.api.boards.entities.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
  boolean existsByIdAndOwnerId(Long boardId, Long ownerId);
}

package com.boards.api.boards.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;

import com.boards.api.boards.entities.Board;

import jakarta.persistence.LockModeType;

public interface BoardRepository extends JpaRepository<Board, Long>{
  boolean existsByIdAndOwnerId(Long boardId, Long ownerId);
  boolean existsByOwner_Id(Long ownerId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Board> findWithLockById(Long boardId);
}

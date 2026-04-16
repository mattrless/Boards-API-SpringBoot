package com.boards.api.cards.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boards.api.cards.entities.CardAssignment;

public interface CardAssignmentRepository extends JpaRepository<CardAssignment, Long>{
  List<CardAssignment> findByCard_Id(Long cardId);

  Optional<CardAssignment> findByCard_IdAndUser_Id(Long cardId, Long userId);

  List<CardAssignment> findByUser_IdAndCard_BoardList_Board_Id(Long userId, Long boardId);
}

package com.boards.api.cards.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boards.api.cards.entities.Card;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long>{
  Optional<Card> findTopByBoardList_IdOrderByPositionDesc(Long boardListId);

  List<Card> findByBoardList_IdOrderByPositionAsc(Long boardListId);

  Optional<Card> findByIdAndBoardList_Id(Long cardId, Long boardListId);

}

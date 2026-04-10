package com.boards.api.boardlists.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boards.api.boardlists.entities.BoardList;

public interface BoardListRepository extends JpaRepository<BoardList, Long>{
  Optional<BoardList> findTopByBoard_IdOrderByPositionDesc(Long boardId);

  List<BoardList> findByBoard_IdOrderByPositionAsc(Long boardId);

  Optional<BoardList> findByIdAndBoard_Id(Long boardListId, Long boardId);
}

package com.boards.api.boards.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boards.api.boards.entities.Board;
import com.boards.api.boards.entities.BoardMember;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long>{
  boolean existsByBoardIdAndUserId(Long boardId, Long userId);

  @EntityGraph(attributePaths = {"boardRole", "boardRole.permissions"})
  Optional<BoardMember> findDetailedByBoardIdAndUserId(Long boardId, Long userId);

  @Query("""
    select bm.board
    from BoardMember bm
    where bm.user.id = :userId
  """)
  List<Board> findBoardsByUserId(@Param("userId") Long userId);

  @EntityGraph(attributePaths = {"boardRole", "boardRole.permissions", "board"})
  Optional<BoardMember> findPermissionsByBoardIdAndUserId(Long boardId, Long userId);
  
  @EntityGraph(attributePaths = {"board", "board.owner", "boardRole"})
  Optional<BoardMember> findByBoardIdAndUserId(Long boardId, Long userId);

  @EntityGraph(attributePaths = {"user", "user.profile", "board", "board.owner", "boardRole"})
  List<BoardMember> findByBoardId(Long boardId);

}

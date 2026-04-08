package com.boards.api.authorization.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boards.api.authorization.entities.BoardRole;
import java.util.Optional;

public interface BoardRoleRepository extends JpaRepository<BoardRole, Long>{
  Optional<BoardRole> findByName(String name);
}

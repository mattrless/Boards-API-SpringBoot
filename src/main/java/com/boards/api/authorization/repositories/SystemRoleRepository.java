package com.boards.api.authorization.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boards.api.authorization.entities.SystemRole;
import java.util.Optional;


public interface SystemRoleRepository extends JpaRepository<SystemRole, Long>{
  Optional<SystemRole> findByName(String name);
}

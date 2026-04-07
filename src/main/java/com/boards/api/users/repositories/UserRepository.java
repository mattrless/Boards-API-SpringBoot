package com.boards.api.users.repositories;

import com.boards.api.users.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  boolean existsByEmail(String email);
  boolean existsByEmailAndIdNot(String email, Long id);

  @EntityGraph(attributePaths = {"profile", "systemRole", "systemRole.permissions"})
  Optional<User> findWithSystemRoleAndPermissionsByEmail(String email);

  @EntityGraph(attributePaths = {"profile", "systemRole", "systemRole.permissions"})
  Optional<User> findWithSystemRoleAndPermissionsById(Long id);
}

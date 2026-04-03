package com.boards.api.users.repositories;

import com.boards.api.users.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  boolean existsByEmail(String email);
  boolean existsByEmailAndIdNot(String email, Long id);
}

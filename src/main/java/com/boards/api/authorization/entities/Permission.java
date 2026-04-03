package com.boards.api.authorization.entities;

import java.util.HashSet;
import java.util.Set;

import com.boards.api.authorization.enums.PermissionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PermissionType type;

  @ManyToMany(mappedBy = "permissions")
  private Set<SystemRole> systemRoles = new HashSet<>();

  @ManyToMany(mappedBy = "permissions")
  private Set<BoardRole> boardRoles = new HashSet<>();
}

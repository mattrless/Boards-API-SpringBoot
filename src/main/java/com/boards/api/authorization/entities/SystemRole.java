package com.boards.api.authorization.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.boards.api.users.entities.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "system_roles")
@Getter
@Setter
@NoArgsConstructor
public class SystemRole {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  @ManyToMany
  @JoinTable(
    name = "system_role_system_permission",
    joinColumns = @JoinColumn(name = "system_role_id"),
    inverseJoinColumns = @JoinColumn(name = "permission_id")
  )
  private Set<Permission> permissions = new HashSet<>();

  @OneToMany(mappedBy = "systemRole")
  private List<User> users = new ArrayList<>();
}

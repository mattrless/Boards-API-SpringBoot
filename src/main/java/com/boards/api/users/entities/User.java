package com.boards.api.users.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.boards.api.authorization.entities.SystemRole;
import com.boards.api.boards.entities.Board;
import com.boards.api.boards.entities.BoardMember;
import com.boards.api.cards.entities.CardAssignment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SoftDelete(columnName = "deleted_at")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @CreatedDate
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }) // opt: (fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id", referencedColumnName = "id", unique = true)
  private Profile profile;

  @ManyToOne(optional = false)
  @JoinColumn(name = "system_role_id", nullable = false)
  private SystemRole systemRole;

  @OneToMany(mappedBy = "owner")
  private List<Board> boards = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<BoardMember> boardMembers = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<CardAssignment> cardAssignments = new ArrayList<>();
}

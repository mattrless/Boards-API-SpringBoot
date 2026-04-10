package com.boards.api.cards.entities;

import com.boards.api.users.entities.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = "card_assignments",
  uniqueConstraints = {
    @UniqueConstraint(name = "uk_card_assignment_card_user", columnNames = {"card_id", "user_id"})
  }
)
@Getter
@Setter
@NoArgsConstructor
public class CardAssignment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;  

  @ManyToOne(optional = false)
  @JoinColumn(name = "card_id", nullable = false)
  private Card card;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}

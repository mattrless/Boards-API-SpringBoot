package com.boards.api.cards.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.boards.api.boardlists.entities.BoardList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
public class Card {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = true, length = 1000)
  private String description;

  @Column(nullable = false)
  private BigDecimal position;

  @ManyToOne(optional = false)
  @JoinColumn(name = "board_list_id", nullable = false)
  private BoardList boardList;

  @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<CardAssignment> cardAssignments = new ArrayList<>();
}

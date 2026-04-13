package com.boards.api.cards.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.boards.api.boards.entities.BoardMember;
import com.boards.api.boards.repositories.BoardMemberRepository;
import com.boards.api.cards.dtos.CreateCardAssignmentDto;
import com.boards.api.cards.entities.Card;
import com.boards.api.cards.entities.CardAssignment;
import com.boards.api.cards.repositories.CardAssignmentRepository;
import com.boards.api.cards.repositories.CardRepository;
import com.boards.api.users.dtos.UserResponseDto;
import com.boards.api.users.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardAssignmentService {
  private final CardAssignmentRepository cardAssignmentRepository;
  private final CardRepository cardRepository;
  private final BoardMemberRepository boardMemberRepository;
  private final UserMapper userMapper;

  @Transactional
  public void addMember(Long boardId, Long cardId, CreateCardAssignmentDto createCardAssignmentDto, Long currentUserId) {
    Card card = cardRepository.findByIdAndBoardList_Board_Id(cardId, boardId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Card not found"  
      )
    );

    Long targetUserId = createCardAssignmentDto.getUserId();

    boolean alreadyAssigned = cardAssignmentRepository.findByCard_IdAndUser_Id(cardId, targetUserId).isPresent();

    if (alreadyAssigned) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User already assigned to card");
    }

    BoardMember currentUserMembership = boardMemberRepository.findDetailedByBoardIdAndUserId(boardId, currentUserId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Logged user is not a member of this board"
      )
    );

    boolean currentUserHasMemberRole = "member".equals(currentUserMembership.getBoardRole().getName());

    if (currentUserHasMemberRole && !currentUserId.equals(targetUserId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Members can only add themselves from a card");
    }
    
    BoardMember targetMember = boardMemberRepository.findDetailedByBoardIdAndUserId(boardId, targetUserId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Member not found"
      )
    );

    CardAssignment cardAssignment = new CardAssignment();
    cardAssignment.setCard(card);
    cardAssignment.setUser(targetMember.getUser());

    cardAssignmentRepository.save(cardAssignment);
  }

  public List<UserResponseDto> findAll(Long boardId, Long cardId) {
    cardRepository.findByIdAndBoardList_Board_Id(cardId, boardId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Card not found"
      )
    );
    
    List<CardAssignment> cardAssignments = cardAssignmentRepository.findByCard_Id(cardId);

    List<UserResponseDto> cardMembers = cardAssignments.stream()
      .map(cm -> userMapper.toResponseDto(cm.getUser()))
      .toList();

    return cardMembers;
  }

  @Transactional
  public void deleteMember(Long boardId, Long cardId, Long currentUserId, Long targetMemberId) {
    BoardMember currentUserMembership = boardMemberRepository.findDetailedByBoardIdAndUserId(boardId, currentUserId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Logged user is not a member of this board"
      )
    );

    // Validate card belongs to board
    cardRepository.findByIdAndBoardList_Board_Id(cardId, boardId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Card not found"
      )
    );

    boolean currentUserHasMemberRole = "member".equals(currentUserMembership.getBoardRole().getName());

    if (currentUserHasMemberRole && !currentUserId.equals(targetMemberId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Members can only remove themselves from a card");
    }

    CardAssignment targetCardAssignment = cardAssignmentRepository.findByCard_IdAndUser_Id(cardId, targetMemberId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Target user not found as card member"
      )
    );

    cardAssignmentRepository.delete(targetCardAssignment);
  }
}

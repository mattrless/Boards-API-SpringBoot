package com.boards.api.cards.dtos;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCardPositionDto {
  @Positive
  private Long prevCardId;
  
  @Positive
  private Long nextCardId;

  @Positive
  private Long targetBoardListId;

  @AssertTrue(message = "Provide prevCardId or nextCardId, or a targetBoardListId when moving without neighbors")
  @Hidden
  public boolean atLeastOneReferenceProvided() {
    return prevCardId != null || nextCardId != null || targetBoardListId != null;
  }
}

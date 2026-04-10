package com.boards.api.boardlists.dtos;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateBoardListPositionDto {
  @Positive
  private Long prevBoardListId;
  
  @Positive
  private Long nextBoardListId;

  @AssertTrue(message = "prevBoardListId or nextBoardListId is required")
  @Hidden // To hide it in swagger
  public boolean isAtLeastOneReferenceProvided() {
    return prevBoardListId != null || nextBoardListId != null;
  }
}

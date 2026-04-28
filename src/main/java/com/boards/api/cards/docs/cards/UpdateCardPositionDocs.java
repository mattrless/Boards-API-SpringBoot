package com.boards.api.cards.docs.cards;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.boards.api.cards.dtos.CardPositionUpdatedResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
  operationId = "updateCardPosition",
  summary = "Update a card position",
  description = """
  Moves a card within the same list or to another list.

  Rules:

  1. Move within the same list:
      - targetBoardListId is optional
      - First position → provide nextCardId
      - Last position → provide prevCardId
      - Between two cards → provide both prevCardId and nextCardId

  2. Move to another list:
      - targetBoardListId is required
      - First position → provide nextCardId
      - Last position → provide prevCardId
      - Between two cards → provide both prevCardId and nextCardId

  3. Move to an empty list:
      - Only targetBoardListId is required

  Notes:
    - At least one of prevCardId, nextCardId, or targetBoardListId must be provided
    - If both prevCardId and nextCardId are provided, the card will be placed between them
    - If only prevCardId is provided, the card will be placed after it
    - If only nextCardId is provided, the card will be placed before it
  """
)
@SecurityRequirement(name = "bearerAuth")
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "200",
    description = "Card position updated successfully.",
    content = @Content(schema = @Schema(implementation = CardPositionUpdatedResponseDto.class))
  ),
  @ApiResponse(
    responseCode = "400",
    description = "Invalid input data or missing required references.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "401",
    description = "Unauthorized.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "403",
    description = "Forbidden: Insufficient permissions.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "404",
    description = "Card not found.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "409",
    description = "Conflict: invalid target order for the requested move (e.g., inconsistent prev/next references).",
    content = @Content
  )
})
public @interface UpdateCardPositionDocs {
}

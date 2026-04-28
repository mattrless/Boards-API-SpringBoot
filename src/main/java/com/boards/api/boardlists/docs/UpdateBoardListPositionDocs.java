package com.boards.api.boardlists.docs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.boards.api.boardlists.dtos.BoardListResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
  operationId = "updateBoardListPosition",
  summary = "Update a board list position",
  description = """
  Reorders a board list within the same board.

  Rules:

    - First position → provide nextBoardListId
    - Last position → provide prevBoardListId
    - Between two lists → provide both prevBoardListId and nextBoardListId

  Notes:
    - At least one of prevBoardListId or nextBoardListId must be provided
    - If both are provided, the list will be placed between them
    - If only prevBoardListId is provided, the list will be placed after it
    - If only nextBoardListId is provided, the list will be placed before it
    - Both references must belong to the same board
  """
)
@SecurityRequirement(name = "bearerAuth")
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "200",
    description = "Board list position updated successfully.",
    content = @Content(schema = @Schema(implementation = BoardListResponseDto.class))
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
    description = "List not found.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "409",
    description = "Conflict: invalid order or inconsistent references.",
    content = @Content
  )
})
public @interface UpdateBoardListPositionDocs {
}

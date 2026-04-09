package com.boards.api.boards.docs.boardmember;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Add a member to a board")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "201",
    description = "Board member added successfully.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "400",
    description = "Invalid input data.",
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
    description = "User, board, or board role not found.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "409",
    description = "Conflict: user is already a member of this board.",
    content = @Content
  )
})
public @interface AddBoardMemberDocs {
}

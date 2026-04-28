package com.boards.api.boards.docs.board;

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
@Operation(operationId = "transferBoardOwnership", summary = "Transfer board ownership to another member")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "200",
    description = "Board ownership transferred successfully.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "401",
    description = "Unauthorized.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "403",
    description = "Forbidden: Only the board owner can transfer ownership.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "404",
    description = "Board not found.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "409",
    description = "Conflict: target user is already the owner or is not a board member.",
    content = @Content
  )
})
public @interface TransferOwnershipDocs {
}

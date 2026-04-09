package com.boards.api.boards.docs.board;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.boards.api.boards.dtos.BoardResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Create a new board")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "201",
    description = "Board created successfully.",
    content = @Content(schema = @Schema(implementation = BoardResponseDto.class))
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
    description = "Owner user not found.",
    content = @Content
  )
})
public @interface CreateBoardDocs {
}

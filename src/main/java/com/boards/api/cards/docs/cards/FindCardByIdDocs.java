package com.boards.api.cards.docs.cards;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.boards.api.cards.dtos.CardResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Get a specific card by ID")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "200",
    description = "Card found and returned.",
    content = @Content(schema = @Schema(implementation = CardResponseDto.class))
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
  )
})
public @interface FindCardByIdDocs {
}

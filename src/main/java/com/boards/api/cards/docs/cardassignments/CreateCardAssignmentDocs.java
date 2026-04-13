package com.boards.api.cards.docs.cardassignments;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
  summary = "Assign a user to a card",
  description = "Allows assigning a board member to a card. Members can only assign themselves."
)
@SecurityRequirement(name = "bearerAuth")
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "201",
    description = "User successfully assigned to the card.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "400",
    description = "Invalid request body.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "401",
    description = "Unauthorized.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "403",
    description = "Forbidden: insufficient permissions.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "404",
    description = "Card not found or user is not a board member.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "409",
    description = "Conflict: user already assigned OR members can only assign themselves.",
    content = @Content
  )
})
public @interface CreateCardAssignmentDocs {
}

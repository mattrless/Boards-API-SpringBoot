package com.boards.api.cards.docs.cardassignments;

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
@Operation(
  operationId = "deleteCardAssignment",
  summary = "Remove a user from a card",
  description = "Removes a user assignment from a card. Members can only remove themselves."
)
@SecurityRequirement(name = "bearerAuth")
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "204",
    description = "User successfully removed from the card.",
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
    description = "Card or assignment not found.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "409",
    description = "Conflict: members can only remove themselves.",
    content = @Content
  )
})
public @interface DeleteCardAssignmentDocs {
  
}

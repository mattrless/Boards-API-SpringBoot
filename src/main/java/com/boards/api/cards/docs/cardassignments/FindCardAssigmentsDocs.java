package com.boards.api.cards.docs.cardassignments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.boards.api.users.dtos.UserResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
  summary = "Get all users assigned to a card",
  description = "Returns a list of users assigned to the specified card."
)
@SecurityRequirement(name = "bearerAuth")
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "200",
    description = "List of assigned users retrieved successfully.",
    content = @Content(
      array = @ArraySchema(
        schema = @Schema(implementation = UserResponseDto.class)
      )
    )
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
    description = "Card not found.",
    content = @Content
  )
})
public @interface FindCardAssigmentsDocs {
}

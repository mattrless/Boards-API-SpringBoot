package com.boards.api.users.docs;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.boards.api.users.dtos.UserResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Create a new user")
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "201",
    description = "The user has been successfully created.",
    content = @Content(schema = @Schema(implementation = UserResponseDto.class))
  ),
  @ApiResponse(
    responseCode = "400",
    description = "Invalid input data.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "409",
    description = "Email already in use.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "500",
    description = "Default system role not found.",
    content = @Content
  )
})
public @interface CreateUserDocs {
  
}

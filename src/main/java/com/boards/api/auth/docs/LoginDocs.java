package com.boards.api.auth.docs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.boards.api.auth.dto.LoginResponseDto;
import com.boards.api.auth.dto.LoginUserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
  operationId = "login",
  summary = "Authenticate a user",
  requestBody = @RequestBody(
    required = true,
    content = @Content(
      schema = @Schema(implementation = LoginUserDto.class),
      examples = {
        @ExampleObject(
          name = "Login request",
          summary = "Email and password credentials",
          value = """
          {
            "email": "admin@boards.com",
            "password": "admin1234"
          }
          """
        )
      }
    )
  )
)
@ApiResponses(value = {
  @ApiResponse(
    responseCode = "200",
    description = "Login successful.",
    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))
  ),
  @ApiResponse(
    responseCode = "400",
    description = "Invalid input data.",
    content = @Content
  ),
  @ApiResponse(
    responseCode = "401",
    description = "Invalid credentials.",
    content = @Content
  )
})
public @interface LoginDocs {
}

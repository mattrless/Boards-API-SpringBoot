package com.boards.api.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.boards.api.auth.docs.LoginDocs;
import com.boards.api.auth.dto.LoginResponseDto;
import com.boards.api.auth.dto.LoginUserDto;
import com.boards.api.auth.services.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  @LoginDocs
  @ResponseStatus(HttpStatus.OK)
  public LoginResponseDto login(@Valid @RequestBody LoginUserDto loginUserDto) {
    return authService.login(loginUserDto);
  }
}

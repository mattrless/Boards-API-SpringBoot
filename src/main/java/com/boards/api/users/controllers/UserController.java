package com.boards.api.users.controllers;

import com.boards.api.security.AuthenticatedUser;
import com.boards.api.users.docs.CreateUserDocs;
import com.boards.api.users.docs.FindAllUsersDocs;
import com.boards.api.users.docs.FindMeDocs;
import com.boards.api.users.docs.FindUserByIdDocs;
import com.boards.api.users.docs.RemoveMeDocs;
import com.boards.api.users.docs.UpdateMeDocs;
import com.boards.api.users.dtos.CreateUserDto;
import com.boards.api.users.dtos.MeResponseDto;
import com.boards.api.users.dtos.UpdateUserDto;
import com.boards.api.users.dtos.UserResponseDto;
import com.boards.api.users.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Users")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  @CreateUserDocs
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto create(@Valid @RequestBody CreateUserDto createUserDto) {
    return userService.create(createUserDto);
  }

  @GetMapping
  @FindAllUsersDocs
  @PreAuthorize("hasAuthority('user_read')")
  public List<UserResponseDto> findAll() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  @FindUserByIdDocs
  @PreAuthorize("hasAuthority('user_read')")
  public UserResponseDto findById(@PathVariable Long id) {
    return userService.findById(id);
  }

  @DeleteMapping("/me")
  @RemoveMeDocs
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('user_delete_self')")
  public void removeMe(@AuthenticationPrincipal AuthenticatedUser currentUser) {
    userService.remove(currentUser.getId());
  }

  @PutMapping("/me")
  @UpdateMeDocs
  @PreAuthorize("hasAuthority('user_update_self')")
  public UserResponseDto update(@AuthenticationPrincipal AuthenticatedUser currentUser, @Valid @RequestBody UpdateUserDto updateUserDto) {
    return userService.update(currentUser.getId(), updateUserDto);
  }

  @GetMapping("/me")
  @FindMeDocs
  public MeResponseDto findMe(@AuthenticationPrincipal AuthenticatedUser currentUser) {
    return userService.findMe(currentUser.getId());
  }
  
}

package com.boards.api.users.controllers;

import com.boards.api.users.dtos.CreateUserDto;
import com.boards.api.users.dtos.UpdateUserDto;
import com.boards.api.users.dtos.UserResponseDto;
import com.boards.api.users.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto create(@Valid @RequestBody CreateUserDto createUserDto) {
    return userService.create(createUserDto);
  }

  @GetMapping
  @PreAuthorize("hasAuthority('user_read')")
  public List<UserResponseDto> findAll() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('user_read')")
  public UserResponseDto findById(@PathVariable Long id) {
    return userService.findById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('user_delete_self')")
  public void remove(@PathVariable Long id) {
    userService.remove(id);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('user_update_self')")
  public UserResponseDto update(@PathVariable Long id, @Valid @RequestBody UpdateUserDto updateUserDto) {
    return userService.update(id, updateUserDto);
  }
}

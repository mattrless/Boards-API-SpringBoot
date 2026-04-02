package com.boards.api.users.controllers;

import com.boards.api.users.dtos.CreateUserDto;
import com.boards.api.users.dtos.UpdateUserDto;
import com.boards.api.users.dtos.UserResponseDto;
import com.boards.api.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto create(@Valid @RequestBody CreateUserDto createUserDto) {
    return userService.create(createUserDto);
  }

  @GetMapping
  public List<UserResponseDto> findAll() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  public UserResponseDto findById(@PathVariable Long id) {
    return userService.findById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remove(@PathVariable Long id) {
    userService.remove(id);
  }

  @PutMapping("/{id}")
  public UserResponseDto update(@PathVariable Long id, @Valid @RequestBody UpdateUserDto updateUserDto) {
    return userService.update(id, updateUserDto);
  }
}

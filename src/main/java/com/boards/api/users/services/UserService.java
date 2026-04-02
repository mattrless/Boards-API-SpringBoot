package com.boards.api.users.services;

import com.boards.api.users.dtos.CreateUserDto;
import com.boards.api.users.dtos.UserResponseDto;
import com.boards.api.users.entities.User;
import com.boards.api.users.mappers.UserMapper;
import com.boards.api.users.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public List<UserResponseDto> findAll() {
    return userRepository.findAll()
            .stream()
            .map(user -> userMapper.toResponseDto(user))
            .toList();
  }

  public UserResponseDto create(CreateUserDto createUserDto) {
    User user = userMapper.toEntity(createUserDto);

    if (userRepository.existsByEmail(createUserDto.getEmail())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
    }
    // hash password
    User savedUser = userRepository.save(user);
    return userMapper.toResponseDto(savedUser);
  }

  public UserResponseDto findById(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
          ));
    return userMapper.toResponseDto(user);
  }
  
  public void remove(Long id) {
    // Alternative
    // if (!userRepository.existsById(id)) {
    //   throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    // }

    // userRepository.deleteById(id);

    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            ));

    userRepository.delete(user);
  }
}
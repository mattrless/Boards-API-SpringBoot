package com.boards.api.users.services;

import com.boards.api.authorization.entities.SystemRole;
import com.boards.api.authorization.repositories.SystemRoleRepository;
import com.boards.api.boards.repositories.BoardRepository;
import com.boards.api.common.exceptions.UserNotFoundException;
import com.boards.api.users.dtos.CreateUserDto;
import com.boards.api.users.dtos.MeResponseDto;
import com.boards.api.users.dtos.UpdateUserDto;
import com.boards.api.users.dtos.UserResponseDto;
import com.boards.api.users.entities.Profile;
import com.boards.api.users.entities.User;
import com.boards.api.users.mappers.UserMapper;
import com.boards.api.users.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // To generate constructor with final properties
public class UserService {
  private final BoardRepository boardRepository;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final SystemRoleRepository systemRoleRepository;

  public List<UserResponseDto> findAll() {
    return userRepository.findAll()
      .stream()
      .map(user -> userMapper.toResponseDto(user))
      .toList();
  }

  @Transactional
  public UserResponseDto create(CreateUserDto createUserDto) {
    if (userRepository.existsByEmail(createUserDto.getEmail())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
    }

    SystemRole userSystemRole = systemRoleRepository.findByName("user")
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Default system role not found"
      )
    );

    User user = userMapper.toEntity(createUserDto);

    user.setPassword(hashPassword(createUserDto.getPassword()));
    user.setSystemRole(userSystemRole);

    User savedUser = userRepository.save(user);
    return userMapper.toResponseDto(savedUser);
  }

  public UserResponseDto findById(Long id) {
    User user = findUserByIdOrThrow(id);
    return userMapper.toResponseDto(user);
  }

  @Transactional
  public UserResponseDto update(Long id, UpdateUserDto updateUserDto) {
    User user = findUserByIdOrThrow(id);

    if (updateUserDto.getEmail() != null) {
      if (userRepository.existsByEmailAndIdNot(updateUserDto.getEmail(), id)) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
      }
      user.setEmail(updateUserDto.getEmail());
    }

    if (updateUserDto.getPassword() != null) {
      user.setPassword(hashPassword(updateUserDto.getPassword()));
    }

    if (updateUserDto.getProfile() != null) {
      if (user.getProfile() == null) {
        user.setProfile(new Profile());
      }
      userMapper.updateProfileEntity(updateUserDto.getProfile(), user.getProfile());
    }

    return userMapper.toResponseDto(userRepository.save(user));
  }

  @Transactional
  public void remove(Long id) {
    // Alternative
    // if (!userRepository.existsById(id)) {
    //   throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    // }

    // userRepository.deleteById(id);

    if (boardRepository.existsByOwner_Id(id)) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Can't delete user, transfer ownership of your owned boards"
      );
    }

    User user = findUserByIdOrThrow(id);
    userRepository.delete(user);
  }

  public MeResponseDto findMe(Long id){
    User user = userRepository.findWithSystemRoleAndPermissionsById(id)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "User not found"
      )
    );

    MeResponseDto response = userMapper.toMeResponseDto(user);
    response.setPermissions(
      user.getSystemRole().getPermissions().stream()
        .map(permission -> permission.getName())
        .collect(Collectors.toSet())
    );
    
    return response;
  }

  private User findUserByIdOrThrow(Long id){
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  private String hashPassword(String password){
    return passwordEncoder.encode(password);
  }
}
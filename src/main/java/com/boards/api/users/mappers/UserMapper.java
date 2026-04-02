package com.boards.api.users.mappers;

import org.mapstruct.Mapper;

import com.boards.api.users.dtos.CreateUserDto;
import com.boards.api.users.dtos.UserResponseDto;
import com.boards.api.users.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponseDto toResponseDto(User user);
  User toEntity(CreateUserDto request);
}

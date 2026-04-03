package com.boards.api.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.boards.api.auth.dto.LoginResponseDto;
import com.boards.api.users.entities.User;
import com.boards.api.users.mappers.UserMapper;

@Mapper(componentModel = "spring", uses = UserMapper.class) // UserMapper since LoginResponseDto has "UserResponseDto user"
public interface AuthMapper {
  @Mapping(target = "access_token", source = "token")
  @Mapping(target = "user", source = "user")
  LoginResponseDto toResponseDto(User user, String token);
}

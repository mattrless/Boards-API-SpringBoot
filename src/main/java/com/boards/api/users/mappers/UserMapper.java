package com.boards.api.users.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.boards.api.authorization.dtos.SystemRoleResponseDto;
import com.boards.api.authorization.entities.SystemRole;
import com.boards.api.users.dtos.CreateProfileDto;
import com.boards.api.users.dtos.CreateUserDto;
import com.boards.api.users.dtos.MeResponseDto;
import com.boards.api.users.dtos.ProfileResponseDto;
import com.boards.api.users.dtos.UpdateProfileDto;
import com.boards.api.users.dtos.UserResponseDto;
import com.boards.api.users.entities.Profile;
import com.boards.api.users.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponseDto toResponseDto(User user);

  // To avoid warnings since converts to entity and the request (body) doesn't have id, createdAt and updatedAt
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  User toEntity(CreateUserDto request);

  ProfileResponseDto toResponseDto(Profile profile);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Profile toEntity(CreateProfileDto request);

  SystemRoleResponseDto toResponseDto(SystemRole systemRole);

  MeResponseDto toMeResponseDto(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateProfileEntity(UpdateProfileDto dto, @MappingTarget Profile profile);
}

package com.boards.api.auth.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.boards.api.auth.dto.LoginResponseDto;
import com.boards.api.auth.dto.LoginUserDto;
import com.boards.api.auth.mapper.AuthMapper;
import com.boards.api.security.JwtUtil;
import com.boards.api.users.entities.User;
import com.boards.api.users.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authManager;
  private final JwtUtil jwtUtil;
  private final AuthMapper authMapper;
  private final UserRepository userRepository;

  
  public LoginResponseDto login(LoginUserDto loginUserDto){
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          loginUserDto.getEmail(),
          loginUserDto.getPassword()
        )
    );

    User user = userRepository.findByEmail(loginUserDto.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    String token = jwtUtil.generateToken(user.getId());
    return authMapper.toResponseDto(user, token);
  }
}

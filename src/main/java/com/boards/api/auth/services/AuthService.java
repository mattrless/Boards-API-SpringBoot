package com.boards.api.auth.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    try {
      authManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          loginUserDto.getEmail(),
          loginUserDto.getPassword()
        )
      );
    } catch (BadCredentialsException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    User user = userRepository.findByEmail(loginUserDto.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    String token = jwtUtil.generateToken(user.getId());
    return authMapper.toResponseDto(user, token);
  }
}

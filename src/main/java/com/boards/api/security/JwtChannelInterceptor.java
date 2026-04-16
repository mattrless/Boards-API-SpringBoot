package com.boards.api.security;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.boards.api.users.entities.User;
import com.boards.api.users.repositories.UserRepository;
import com.boards.api.websockets.entities.WebSocketPrincipal;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (accessor == null) {
      return message;
    }

    if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
      return message;
    }

    String authHeader = accessor.getFirstNativeHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new IllegalArgumentException("Missing or invalid Authorization header");
    }

    String token = authHeader.substring(7);

    if (!jwtUtil.validateJwtToken(token)) {
      throw new IllegalArgumentException("Invalid or expired token");
    }

    Long userId = jwtUtil.extractUserId(token);

    User user = userRepository.findWithSystemRoleAndPermissionsById(userId)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    UserDetails userDetails = customUserDetailsService.buildUserDetails(user);

    AuthenticatedUser authenticatedUser = (AuthenticatedUser) userDetails;

    accessor.setUser(new WebSocketPrincipal(user.getId(), authenticatedUser));
    accessor.getSessionAttributes().put("userId", user.getId());

    return message;
  }
}

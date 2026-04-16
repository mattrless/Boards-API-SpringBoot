package com.boards.api.websockets.entities;

import java.security.Principal;

import com.boards.api.security.AuthenticatedUser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WebSocketPrincipal implements Principal{
  private final Long userId;
  private final AuthenticatedUser authenticatedUser;
  
  @Override
  public String getName() {
    return String.valueOf(userId);
  }
}

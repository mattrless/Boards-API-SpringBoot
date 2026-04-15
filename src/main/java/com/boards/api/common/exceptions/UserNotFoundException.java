package com.boards.api.common.exceptions;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("User not found");
  }
}

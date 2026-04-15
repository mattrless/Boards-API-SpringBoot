package com.boards.api.common.exceptions;

public class BoardNotFoundException extends RuntimeException {
  public BoardNotFoundException() {
    super("Board not found");
  }
}

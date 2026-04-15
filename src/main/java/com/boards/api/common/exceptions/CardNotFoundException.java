package com.boards.api.common.exceptions;

public class CardNotFoundException extends RuntimeException {
  public CardNotFoundException() {
    super("Card not found");
  }
}

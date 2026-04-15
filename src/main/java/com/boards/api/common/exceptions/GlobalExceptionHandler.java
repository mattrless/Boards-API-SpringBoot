package com.boards.api.common.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CardNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleCardNotFoundException(CardNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
      "status", 404,
      "error", "Not Found",
      "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
      "status", 404,
      "error", "Not Found",
      "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(BoardNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleBoardNotFoundException(BoardNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
      "status", 404,
      "error", "Not Found",
      "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> fields = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error -> {
      fields.put(error.getField(), error.getDefaultMessage());
    });

    Map<String, Object> response = new HashMap<>();
    response.put("status", 400);
    response.put("error", "Bad Request");
    response.put("fields", fields);

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    String message = "Database constraint violation";

    if (ex.getMostSpecificCause() != null &&
      ex.getMostSpecificCause().getMessage().contains("users_email_key")) {
      message = "Email already in use";
    }

    return ResponseEntity.status(409).body(Map.of(
        "status", 409,
        "error", "Conflict",
        "message", message
      )
    );
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
    return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
        "status", ex.getStatusCode().value(),
        "error", ex.getStatusCode().toString(),
        "message", ex.getReason() != null ? ex.getReason() : "Request failed"
      )
    );
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    return ResponseEntity.badRequest().body(Map.of(
      "status", 400,
      "error", "Bad Request",
      "message", "Malformed JSON request"
    ));
  }

}

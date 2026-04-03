package com.boards.api.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

  @Value("${application.security.jwt.secret}")
  private String secret;

  @Value("${application.security.jwt.expiration}")
  private long expires_in;

  private SecretKey getKey() {
      return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(Long userId) {
      return Jwts.builder()
              .subject(String.valueOf(userId))
              .issuedAt(new Date())
              .expiration(new Date(System.currentTimeMillis() + expires_in))
              .signWith(getKey())
              .compact();
  }

  public Long extractUserId(String token) {
    String subject =  Jwts.parser()
                        .verifyWith(getKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject();
    return Long.valueOf(subject);
  }

  public boolean validateJwtToken(String token) {
      try {
          extractUserId(token);
          return true;
      } catch (Exception e) {
          return false;
      }
  }
}

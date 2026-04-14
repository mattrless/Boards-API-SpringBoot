package com.boards.api.ai.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.boards.api.ai.docs.CheckGrammarDocs;
import com.boards.api.ai.docs.GenerateDescriptionDocs;
import com.boards.api.ai.dtos.CheckGrammarDto;
import com.boards.api.ai.dtos.DescriptionResponseDto;
import com.boards.api.ai.dtos.GenerateDescriptionDto;
import com.boards.api.ai.services.GeminiService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {
  private final GeminiService geminiService;

  @PostMapping("/generate-description")
  @GenerateDescriptionDocs
  public DescriptionResponseDto generateDescription(
    @Valid @RequestBody GenerateDescriptionDto generateDescriptionDto
  ) {
    try {
      return geminiService.generateDescription(generateDescriptionDto.getTitle());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Ai service temporarily unavailable");
    }
  }

  @PostMapping("/check-grammar")
  @CheckGrammarDocs
  public DescriptionResponseDto checkGrammar(
    @Valid @RequestBody CheckGrammarDto checkGrammarDto
  ) {
    try {
      return geminiService.checkDescriptionGrammar(checkGrammarDto.getDescription());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Ai service temporarily unavailable");
    }
  }
  
}

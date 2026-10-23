package com.spring_ai_ollama.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/spring-ai/api-chat")
public class ChatController {

    private final ChatClient chatClient;
    // Using an autoconfigured ChatClient.Builder, https://docs.spring.io/spring-ai/reference/api/chatclient.html#_using_an_autoconfigured_chatclient_builder
    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "Hello! Tell me a fun fact.") String message) {
        return this.chatClient.prompt()
            .user(message)
            .call()
            .content();
    }
    /*
// 1. Handles API Timeouts, Rate Limits, or Downstream AI Provider issues
    @ExceptionHandler(TransientAiException.class)
    public ResponseEntity<ErrorResponse> handleAiTransientException(TransientAiException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "AI service is temporarily unavailable. Please try again later.",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // 2. Handles unexpected runtime errors (Fallback catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred while processing your AI request.",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // A clean, structured format for your API error payloads
    public record ErrorResponse(int status, String message, String details, LocalDateTime timestamp) {}
*/}

package com.spring_ai_ollama.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatControllerMockitoTest {

    // Mock the builder and the fluid API components
    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec responseSpec;

    private ChatController chatController;

    @BeforeEach
    void setUp() {
        // Stub the builder chain so chatClientBuilder.build() returns our mock client
        when(chatClientBuilder.build()).thenReturn(chatClient);

        // Inject the mocked builder into the controller (Line 35 fix)
        chatController = new ChatController(chatClientBuilder);
    }

    @Test
    void testGenerate_Success() {
        // Arrange
        String userMessage = "Hello! Tell me a fun fact.";
        String expectedResponse = "Honey never spoils.";

        // Stub the entire fluid API chain: prompt() -> user() -> call() -> content()
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(expectedResponse);

        // Act
        String actualResponse = chatController.generate(userMessage);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }
}

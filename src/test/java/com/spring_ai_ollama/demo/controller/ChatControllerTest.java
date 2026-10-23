package com.spring_ai_ollama.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Use org.springframework.boot.test.mock.mockito.MockBean if Boot < 3.4
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ai.chat.client.ChatClient;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class) // Only loads the web layer and ChatController
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Automatically mocks and registers this bean into the application context
    private ChatClient.Builder chatClientBuilder;

    // Fluid API mocks
    @MockitoBean private ChatClient chatClient;
    @MockitoBean private ChatClient.ChatClientRequestSpec requestSpec;
    @MockitoBean private ChatClient.CallResponseSpec responseSpec;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void setUp() {
        // Fixes the NPE in the controller's constructor
        when(chatClientBuilder.build()).thenReturn(chatClient);

        // Initialize mocks and inject them into MyController
        MockitoAnnotations.openMocks(this);
        
        // Set up MockMvc standalone for the controller
        this.mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();

        // Setup the baseline fluid API chain behavior
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
    }

    // ==========================================
    // VALID TEST CASES
    // ==========================================

    @Test
    void testGenerate_WithCustomMessage_ReturnsValidResponse() throws Exception {
        String customMessage = "Why is the sky blue?";
        String expectedResponse = "Because of Rayleigh scattering.";
        
        when(responseSpec.content()).thenReturn(expectedResponse);

        mockMvc.perform(get("/spring-ai/api-chat/generate")
                .param("message", customMessage))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void testGenerate_WithDefaultMessage_ReturnsValidResponse() throws Exception {
        String expectedResponse = "Honey never spoils.";
        
        when(responseSpec.content()).thenReturn(expectedResponse);

        // Not providing a "message" parameter triggers the default method parameter value
        mockMvc.perform(get("/spring-ai/api-chat/generate"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    // ==========================================
    // EXCEPTION TEST CASES
    // ==========================================

   /* @Test
    void testGenerate_WhenAiServiceThrowsException_Returns500InternalServerError() throws Exception {
        // Mock a failure down the fluid API chain (e.g., API timeout or service down)
        when(responseSpec.content()).thenThrow(new RuntimeException("Ollama service unavailable"));

        mockMvc.perform(get("/spring-ai/api-chat/generate"))
                .andExpect(status().isInternalServerError()); 
                // Depending on your GlobalExceptionHandler, you can also assert error JSON content here
    }*/
}

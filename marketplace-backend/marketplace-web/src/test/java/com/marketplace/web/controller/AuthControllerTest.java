package com.marketplace.web.controller;

import com.marketplace.api.dto.auth.JwtResponse;
import com.marketplace.api.dto.auth.LoginRequest;
import com.marketplace.api.dto.auth.RefreshTokenRequest;
import com.marketplace.api.dto.auth.RegisterRequest;
import com.marketplace.api.dto.exception.ExceptionResponse;
import com.marketplace.api.service.AuthService;
import com.marketplace.web.configuration.WebMvcBaseTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = AuthController.class)
class AuthControllerTest extends WebMvcBaseTest {

    @MockitoBean
    private AuthService authService;

    @Nested
    class Register {
        @Test
        void register_ShouldReturnCreatedStatus() throws Exception {
            // Given
            RegisterRequest registerRequest = new RegisterRequest(
                    "test@example.com",
                    "Password123!",
                    "John",
                    "Doe"
            );

            JwtResponse expectedResponse = new JwtResponse(
                    "access_token",
                    "refresh_token"
            );

            when(authService.register(any(RegisterRequest.class)))
                    .thenReturn(expectedResponse);

            // When & Then
            MvcResult result = mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerRequest)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();
        }

        @Test
        void whenInvalidEmail_thenReturnBadRequest() throws Exception {
            // Given
            RegisterRequest registerRequest = new RegisterRequest(
                    "invalidEmail",  // Email invalide
                    "Password123!",
                    "John",
                    "Doe"
            );

            String content = objectMapper.writeValueAsString(registerRequest);

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void whenInvalidPassword_thenReturnBadRequest() throws Exception {
            // Given
            RegisterRequest registerRequest = new RegisterRequest(
                    "test@example.com",
                    "weak",  // Mot de passe invalide
                    "John",
                    "Doe"
            );

            String content = objectMapper.writeValueAsString(registerRequest);

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void whenInvalidFirstname_thenReturnBadRequest() throws Exception {
            // Given
            RegisterRequest registerRequest = new RegisterRequest(
                    "test@example.com",
                    "Password123!",
                    "John123", // Prénom invalide avec des chiffres
                    "Doe"
            );

            String content = objectMapper.writeValueAsString(registerRequest);

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void whenInvalidLastname_thenReturnBadRequest() throws Exception {
            // Given
            RegisterRequest registerRequest = new RegisterRequest(
                    "test@example.com",
                    "Password123!",
                    "John",
                    "Doe123" // Nom invalide avec des chiffres
            );

            String content = objectMapper.writeValueAsString(registerRequest);

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class Login {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            LoginRequest request = new LoginRequest(
                    "test@example.com",
                    "Password123!"
            );
            JwtResponse expectedResponse = new JwtResponse("access_token", "refresh_token");

            when(authService.authenticate(any(LoginRequest.class)))
                    .thenReturn(expectedResponse);

            // When & Then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("access_token"))
                    .andExpect(jsonPath("$.refreshToken").value("refresh_token"));
        }

        @Test
        void whenEmptyEmail_thenReturnBadRequest() throws Exception {
            // Given
            LoginRequest request = new LoginRequest(
                    "",
                    "Password123!"
            );

            // When & Then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void whenEmptyPassword_thenReturnBadRequest() throws Exception {
            // Given
            LoginRequest request = new LoginRequest(
                    "test@example.com",
                    ""
            );

            // When & Then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class RefreshToken {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            RefreshTokenRequest request = new RefreshTokenRequest("valid-refresh-token");
            JwtResponse expectedResponse = new JwtResponse("new_access_token", "new_refresh_token");

            when(authService.refreshToken(any(String.class)))
                    .thenReturn(expectedResponse);

            // When & Then
            mockMvc.perform(post("/auth/refresh-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("new_access_token"))
                    .andExpect(jsonPath("$.refreshToken").value("new_refresh_token"));
        }

        @Test
        void whenEmptyToken_thenReturnBadRequest() throws Exception {
            // Given
            RefreshTokenRequest request = new RefreshTokenRequest("");

            // When & Then
            mockMvc.perform(post("/auth/refresh-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}
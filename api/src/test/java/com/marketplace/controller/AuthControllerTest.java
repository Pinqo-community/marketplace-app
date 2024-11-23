package com.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.annotation.ControllerWebMvcTest;
import com.marketplace.dto.*;
import com.marketplace.entity.User;
import com.marketplace.service.JwtService;
import com.marketplace.service.AuthService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerWebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class Register {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            // Given
            RegisterRequest request = new RegisterRequest(
                    "test@email.com",
                    "Password123!",
                    "John",
                    "Doe"
            );
            JwtResponse jwtResponse = mock(JwtResponse.class);
            User user = mock(User.class);

            when(authService.register(any())).thenReturn(user);
            when(jwtService.generateJwtToken(any())).thenReturn(jwtResponse);

            // When
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                            .andExpect(status().isCreated());
        }

        @Test
        void whenInvalidEmail_thendReturnBadRequest() throws Exception {
            // Given
            RegisterRequest request = new RegisterRequest(
                    "invalidEmail",
                    "Password123!",
                    "John",
                    "Doe"
            );

            // When
            MvcResult result = mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // Then
            String contentAsString = result.getResponse().getContentAsString();
            ExceptionResponse actualResponse = objectMapper.readValue(contentAsString, ExceptionResponse.class);

            assertThat(actualResponse)
                    .isNotNull()
                    .isInstanceOf(ExceptionResponse.class);
        }

        @Test
        void whenInvalidPassword_thenReturnBadRequest() throws Exception {
            RegisterRequest request = new RegisterRequest(
                    "test@test.com",
                    "wrongPassword",
                    "John",
                    "Doe"
            );

            // When
            MvcResult result = mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // Then
            String contentAsString = result.getResponse().getContentAsString();
            ExceptionResponse actualResponse = objectMapper.readValue(contentAsString, ExceptionResponse.class);

            assertThat(actualResponse)
                    .isNotNull()
                    .isInstanceOf(ExceptionResponse.class);
        }

        @Test
        void whenInvalidFirstname_thenReturnBadRequest() throws Exception {
            // Given
            RegisterRequest request = new RegisterRequest(
                    "test@test.com",
                    "Password123!",
                    "John31",
                    "Doe"
            );

            // When
            MvcResult result = mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // Then
            String contentAsString = result.getResponse().getContentAsString();
            ExceptionResponse actualResponse = objectMapper.readValue(contentAsString, ExceptionResponse.class);

            assertThat(actualResponse)
                    .isNotNull()
                    .isInstanceOf(ExceptionResponse.class);
        }

        @Test
        void whenInvalidLastname_thenReturnBadRequest() throws Exception {
            // Given
            RegisterRequest request = new RegisterRequest(
                    "test@test.com",
                    "Password123!",
                    "John",
                    "Doe31"
            );

            // When
            MvcResult result = mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            // Then
            String contentAsString = result.getResponse().getContentAsString();
            ExceptionResponse actualResponse = objectMapper.readValue(contentAsString, ExceptionResponse.class);

            assertThat(actualResponse)
                    .isNotNull()
                    .isInstanceOf(ExceptionResponse.class);
        }
    }

    @Nested
    class Login {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            LoginRequest request = new LoginRequest(
                    "test@test.com",
                    "Password123!"
            );
            JwtResponse jwtResponse = mock(JwtResponse.class);
            User user = mock(User.class);

            when(authService.authenticate(any())).thenReturn(user);
            when(jwtService.generateJwtToken(any())).thenReturn(jwtResponse);

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        void whenEmptyEmail_thenReturnBadRequest() throws Exception {
            LoginRequest request = new LoginRequest(
                    "",
                    "Password123!"
            );

            MvcResult result = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String contentAsString = result.getResponse().getContentAsString();
            ExceptionResponse actualResponse = objectMapper.readValue(contentAsString, ExceptionResponse.class);

            assertThat(actualResponse)
                    .isNotNull()
                    .isInstanceOf(ExceptionResponse.class);
        }

        @Test
        void whenEmptyPassword_thenReturnBadRequest() throws Exception {
            LoginRequest request = new LoginRequest(
                    "test@test.com",
                    ""
            );

            MvcResult result = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String contentAsString = result.getResponse().getContentAsString();
            ExceptionResponse actualResponse = objectMapper.readValue(contentAsString, ExceptionResponse.class);

            assertThat(actualResponse)
                    .isNotNull()
                    .isInstanceOf(ExceptionResponse.class);
        }
    }

    @Nested
    class RefreshToken {
        @Test
        void whenValidData_thenReturnSuccess() throws Exception {
            RefreshTokenRequest request = new RefreshTokenRequest(
                    "refresh-token"
            );
            JwtResponse jwtResponse = mock(JwtResponse.class);
            User user = mock(User.class);

            when(authService.refreshToken(any())).thenReturn(user);
            when(jwtService.generateJwtToken(any())).thenReturn(jwtResponse);

            mockMvc.perform(post("/auth/refresh-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        void whenEmptyToken_thenReturnBadRequest() throws Exception {
            RefreshTokenRequest request = new RefreshTokenRequest(
                    ""
            );

            MvcResult result = mockMvc.perform(post("/auth/refresh-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String contentAsString = result.getResponse().getContentAsString();
            ExceptionResponse actualResponse = objectMapper.readValue(contentAsString, ExceptionResponse.class);

            assertThat(actualResponse)
                    .isNotNull()
                    .isInstanceOf(ExceptionResponse.class);
        }
    }
}
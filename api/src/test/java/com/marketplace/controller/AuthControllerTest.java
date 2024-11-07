package com.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.annotation.ControllerWebMvcTest;
import com.marketplace.dto.*;
import com.marketplace.entity.Buyer;
import com.marketplace.entity.Role;
import com.marketplace.entity.User;
import com.marketplace.exception.InvalidTokenException;
import com.marketplace.exception.UserAlreadyExistsException;
import com.marketplace.model.RoleType;
import com.marketplace.security.jwt.JwtService;
import com.marketplace.service.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private User user;
    private JwtResponse jwtResponse;

    @BeforeEach
    public void setup() {
        jwtResponse = new JwtResponse("jwt-token", "refresh-token");
        user = User.builder()
                .email("test@test.com")
                .password("Password123!")
                .buyer(
                        Buyer.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .build()
                )
                .provider("local")
                .enabled(true)
                .roles(new HashSet<>(Set.of(new Role(RoleType.ROLE_USER))))
                .build();
    }

    /*
     * REGISTER OPERATION TESTS
     */
    @Test
    void register_WithValidData_ShouldReturnSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "test@test.com",
                "Password123!",
                "John",
                "Doe"
        );

        when(authService.register(any())).thenReturn(user);
        when(jwtService.generateJwtToken(any())).thenReturn(jwtResponse);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void register_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "invalid-email",
                "Password123!",
                "John",
                "Doe"
        );

        MvcResult result = mockMvc.perform(post("/auth/register")
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
    void register_WithExistingEmail_ShouldReturnConflict() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "test@test.com",
                "Password123!",
                "John",
                "Doe"
        );

        when(authService.register(any()))
                .thenThrow(new UserAlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void register_WhenServiceThrowsException_ShouldReturnInternalError() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "test@test.com",
                "Password123!",
                "John",
                "Doe"
        );

        when(authService.register(any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    /*
     * LOGIN OPERATION TESTS
     */
    @Test
    void login_WithValidData_ShouldReturnSuccess() throws Exception {
        LoginRequest request = new LoginRequest(
                "test@test.com",
                "Password123!"
        );

        when(authService.authenticate(any())).thenReturn(user);
        when(jwtService.generateJwtToken(any())).thenReturn(jwtResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void login_WithEmailEmail_ShouldReturnBadRequest() throws Exception {
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
    void login_WithEmailPassword_ShouldReturnBadRequest() throws Exception {
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

    /*
     * REFRESH TOKEN OPERATION TESTS
     */
    @Test
    void refreshToken_WithValidToken_ShouldReturnNewTokens() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest(
                "refresh-token"
        );

        when(authService.refreshToken(any())).thenReturn(user);
        when(jwtService.generateJwtToken(any())).thenReturn(jwtResponse);

        mockMvc.perform(post("/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void refreshToken_WithEmptyToken_ShouldReturnBadRequest() throws Exception {
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

    @Test
    void refreshToken_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("invalid-token");

        when(authService.refreshToken(any()))
                .thenThrow(new InvalidTokenException("Invalid refresh token"));

        // When & Then
        mockMvc.perform(post("/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid refresh token"));
    }
}
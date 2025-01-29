package com.marketplace.web.controller;

import com.marketplace.api.dto.auth.JwtResponse;
import com.marketplace.api.dto.auth.LoginRequest;
import com.marketplace.api.dto.auth.RefreshTokenRequest;
import com.marketplace.api.dto.auth.RegisterRequest;
import com.marketplace.api.dto.exception.ExceptionResponse;
import com.marketplace.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 * Handles user registration, login, and token refresh.
 */
@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication operations including registration, login, and token refresh")
public class AuthController {
    private final AuthService authService;

    /**
     * Registers a new user.
     *
     * @param request registration information
     * @return ResponseEntity containing JWT tokens
     */
    @Operation(
            summary = "Register new user",
            description = "Creates a new user account and returns access and refresh tokens"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid registration data",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(
            @Valid
            @RequestBody
            @Parameter(description = "User registration details", required = true)
            RegisterRequest request
    ) {
        try {
            log.atInfo().log("POST /auth/register - START: Registering new user");
            JwtResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            log.atInfo().log("POST /auth/register - END");
        }
    }

    /**
     * Authenticates a user.
     *
     * @param request login credentials
     * @return ResponseEntity containing JWT tokens
     */
    @Operation(
            summary = "Login user",
            description = "Authenticates user credentials and returns access and refresh tokens"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid
            @RequestBody
            @Parameter(description = "User login credentials", required = true)
            LoginRequest request
    ) {
        try {
            log.atInfo().log("POST /auth/login - START: Authenticating user");
            JwtResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } finally {
            log.atInfo().log("POST /auth/login - END");
        }
    }

    /**
     * Refreshes authentication tokens.
     *
     * @param request refresh token
     * @return ResponseEntity containing new JWT tokens
     */
    @Operation(
            summary = "Refresh token",
            description = "Generates new access and refresh tokens using a valid refresh token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tokens refreshed successfully",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(
            @Valid
            @RequestBody
            @Parameter(description = "Current refresh token", required = true)
            RefreshTokenRequest request
    ) {
        try {
            log.atInfo().log("POST /auth/refresh-token - START: Refreshing tokens");
            JwtResponse response = authService.refreshToken(request.refreshToken());
            return ResponseEntity.ok(response);
        } finally {
            log.atInfo().log("POST /auth/refresh-token - END");
        }
    }
}

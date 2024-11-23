package com.marketplace.controller;

import com.marketplace.dto.*;
import com.marketplace.entity.User;
import com.marketplace.service.JwtService;
import com.marketplace.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations related to user authentication including registration, login, token refresh")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;


    @Operation(
            summary = "Register new user",
            description = "Create a new user account and return access token and refresh token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(
            @Valid
            @RequestBody
            @Parameter(
                    description = "Register informations",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class)
                    )
            )
            RegisterRequest request
    ) {
        try {
            log.info("POST /auth/register - START");
            User userRegistred = authService.register(request);
            JwtResponse response = jwtService.generateJwtToken(userRegistred);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            log.info("POST /auth/register - DONE");
        }
    }

    @Operation(
            summary = "Login user",
            description = "Authenticate a user and return access token and refresh token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully authenticated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid
            @RequestBody
            @Parameter(
                    description = "Login credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class)
                    )
            )
            LoginRequest request
    ) {
        try {
            log.info(" POST /auth/login - START");
            User userAuthenticated = authService.authenticate(request);
            JwtResponse response = jwtService.generateJwtToken(userAuthenticated);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            log.info(" POST /auth/login - DONE");
        }
    }

    @Operation(
            summary = "Refresh token",
            description = "Verify token and return new access token and refresh token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token successfully refreshed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token"
            )
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(
            @Valid
            @RequestBody
            @Parameter(
                    description = "Refresh token",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequest.class)
                    )
            )
            RefreshTokenRequest request
    ) {
        try {
            log.info("POST /auth/refresh-token - START");
            User userFromToken = authService.refreshToken(request.refreshToken());
            JwtResponse response = jwtService.generateJwtToken(userFromToken);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            log.info("POST /auth/refresh-token - DONE");
        }
    }
}

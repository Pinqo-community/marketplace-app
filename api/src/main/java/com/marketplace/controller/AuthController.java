package com.marketplace.controller;

import com.marketplace.dto.JwtResponse;
import com.marketplace.dto.LoginRequest;
import com.marketplace.dto.RefreshTokenRequest;
import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.User;
import com.marketplace.security.jwt.JwtService;
import com.marketplace.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        try {
            log.info("POST /auth/register - START");
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    authService.register(request)
            );
        } finally {
            log.info("POST /auth/register - DONE");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody LoginRequest request) {
        try {
            log.info(" POST /auth/login - START");
            User userAuthenticated = authService.authenticate(request);
            JwtResponse response = jwtService.generateJwtToken(userAuthenticated);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            log.info(" POST /auth/login - DONE");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
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

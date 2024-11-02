package com.marketplace.controller;

import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.User;
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
}

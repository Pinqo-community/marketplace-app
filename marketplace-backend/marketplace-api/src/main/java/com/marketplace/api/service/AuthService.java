package com.marketplace.api.service;

import com.marketplace.api.dto.auth.JwtResponse;
import com.marketplace.api.dto.auth.LoginRequest;
import com.marketplace.api.dto.auth.RegisterRequest;

public interface AuthService {
    JwtResponse register(RegisterRequest request);

    JwtResponse authenticate(LoginRequest request);

    JwtResponse refreshToken(String refreshToken);
}

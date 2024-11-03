package com.marketplace.service;

import com.marketplace.dto.LoginRequest;
import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.User;

public interface AuthService {
    User register(RegisterRequest request);

    User authenticate(LoginRequest request);

    User refreshToken(String refreshToken);
}

package com.marketplace.service;

import com.marketplace.dto.LoginRequest;
import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.User;
import jakarta.transaction.Transactional;

public interface AuthService {
    @Transactional
    User register(RegisterRequest request);

    User authenticate(LoginRequest request);

    User refreshToken(String refreshToken);
}

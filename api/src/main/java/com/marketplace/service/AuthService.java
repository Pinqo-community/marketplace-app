package com.marketplace.service;

import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.User;

public interface AuthService {
    User register(RegisterRequest request);
}

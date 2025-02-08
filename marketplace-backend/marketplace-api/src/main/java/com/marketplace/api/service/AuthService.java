package com.marketplace.api.service;

import com.marketplace.api.dto.auth.JwtResponse;
import com.marketplace.api.dto.auth.LoginRequest;
import com.marketplace.api.dto.auth.RegisterRequest;

/**
 * Service interface for handling authentication and authorization operations.
 * Provides functionality for user registration, authentication, and token management.
 */
public interface AuthService {

    /**
     * Registers a new user in the system.
     *
     * @param request The registration request containing user details
     * @return JwtResponse containing access token, refresh token
     */
    JwtResponse register(RegisterRequest request);

    /**
     * Authenticates a user with their credentials.
     *
     * @param request The login request containing user credentials (username/email and password)
     * @return JwtResponse containing access token, refresh token
     */
    JwtResponse authenticate(LoginRequest request);

    /**
     * Generates a new access token using a valid refresh token.
     *
     * @param refreshToken The refresh token string used to generate a new access token
     * @return JwtResponse containing the new access token, refresh token
     */
    JwtResponse refreshToken(String refreshToken);
}

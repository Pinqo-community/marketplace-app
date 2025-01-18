package com.marketplace.auth.service.impl;

import com.marketplace.api.dto.UserDTO;
import com.marketplace.api.dto.auth.JwtResponse;
import com.marketplace.api.dto.auth.LoginRequest;
import com.marketplace.api.dto.auth.RegisterRequest;
import com.marketplace.api.exception.InvalidTokenException;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.api.service.AuthService;
import com.marketplace.api.service.UserService;
import com.marketplace.api.exception.WrongCredentialException;
import com.marketplace.auth.entity.InvalidRefreshToken;
import com.marketplace.auth.model.BasicUserInfos;
import com.marketplace.auth.repository.InvalidRefreshTokenRepository;
import com.marketplace.auth.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final InvalidRefreshTokenRepository invalidRefreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user and generates authentication tokens
     *
     * @param request User registration information
     * @return JWT access and refresh tokens
     */
    @Override
    public JwtResponse register(RegisterRequest request) {
        log.debug("Enter register(request = {})", request);

        BasicUserInfos userInfos = new BasicUserInfos(request.email(), passwordEncoder.encode(request.password()), request.firstname(), request.lastname());
        UserDTO userRegistred = userService.createUser(userInfos);
        JwtResponse jwtResponse = jwtService.generateJwtToken(userRegistred);

        log.debug("Leave register() - return {}", jwtResponse);

        return jwtResponse;
    }

    /**
     * Authenticates a user with credentials and generates tokens
     *
     * @param request Login credentials
     * @return JWT access and refresh tokens
     * @throws WrongCredentialException if credentials are invalid
     */
    @Override
    public JwtResponse authenticate(LoginRequest request) {
        log.debug("Enter authenticate(request = {})", request);

        UserDTO user = validateUserCredentials(request);

        JwtResponse jwtResponse = jwtService.generateJwtToken(user);

        log.debug("Leave authenticate() - return {}", jwtResponse);
        return jwtResponse;
    }

    /**
     * Generates new tokens from a valid refresh token
     *
     * @param refreshToken Token to validate
     * @return New JWT tokens
     * @throws InvalidTokenException if token is invalid or expired
     */
    @Override
    public JwtResponse refreshToken(String refreshToken) {
        log.debug("Enter refreshToken(refreshToken = {})", refreshToken);

        validateRefreshToken(refreshToken);
        invalidateRefreshToken(refreshToken);

        Long userId = Long.parseLong(jwtService.extractSubject(refreshToken));

        try {
            UserDTO user = userService.getUserById(userId);
            JwtResponse jwtResponse = jwtService.generateJwtToken(user);
            log.debug("Leave refreshToken() - return {}", jwtResponse);
            return jwtResponse;
        } catch (NotFoundException e) {
            log.error("User not found during token refresh: {}", e.getMessage());
            throw new InvalidTokenException("Token invalide");
        }
    }

    /**
     * Validates a refresh token's status
     *
     * @param refreshToken Token to validate
     * @throws InvalidTokenException if token is invalid or expired
     */
    private void validateRefreshToken(String refreshToken) {
        log.debug("Enter validateRefreshToken(refreshToken = {})", refreshToken);

        if (jwtService.isExpired(refreshToken)
                || !jwtService.extractClaimValue(refreshToken, "typ").equals("Refresh")
                || invalidRefreshTokenRepository.existsByToken(refreshToken)) {
            log.error("Invalid refresh token");
            throw new InvalidTokenException("Token invalide");
        }

        log.debug("Leave validateRefreshToken()");
    }

    /**
     * Blacklists a refresh token to prevent reuse
     *
     * @param refreshToken Token to invalidate
     */
    private void invalidateRefreshToken(String refreshToken) {
        log.debug("Enter invalidateRefreshToken(refreshToken = {})", refreshToken);

        invalidRefreshTokenRepository.save(
                InvalidRefreshToken.builder()
                        .token(refreshToken)
                        .expiryDate(jwtService.extractExpiration(refreshToken))
                        .build()
        );

        log.debug("Leave invalidateRefreshToken()");
    }

    /**
     * Verifies user credentials and retrieves user information
     *
     * @param request Credentials to verify
     * @return User information
     * @throws WrongCredentialException if credentials are invalid
     * @throws NotFoundException if user does not exist
     */
    private UserDTO validateUserCredentials(LoginRequest request) {
        try {
            log.debug("Enter validateUserCredentials(request = {})", request);

            UserDTO user = userService.getUserByEmail(request.email());

            if (!user.provider().equalsIgnoreCase("local")) {
                throw new WrongCredentialException("Les identifiants sont invalides");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            log.debug("Leave validateUserCredentials() - return {}", user);

            return user;

        } catch (AuthenticationException | NotFoundException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new WrongCredentialException("Les identifiants sont invalides");
        }
    }
}

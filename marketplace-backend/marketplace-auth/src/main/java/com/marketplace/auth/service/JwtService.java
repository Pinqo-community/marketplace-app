package com.marketplace.auth.service;

import com.marketplace.api.dto.auth.JwtResponse;
import com.marketplace.api.dto.user.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * Service interface for JWT (JSON Web Token) operations.
 * Handles token generation, validation, and parsing.
 */
public interface JwtService {
    /**
     * Generates JWT access and refresh tokens for a user.
     *
     * @param user the user for whom to generate tokens
     * @return response containing access and refresh tokens
     */
    JwtResponse generateJwtToken(UserDTO user);

    /**
     * Extracts the subject claim from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the subject (typically user ID) from the token
     */
    String extractSubject(String token);

    /**
     * Extracts a specific claim value from a JWT token.
     *
     * @param token the JWT token to parse
     * @param claimName the name of the claim to extract
     * @return the value of the specified claim
     */
    Object extractClaimValue(String token, String claimName);

    /**
     * Validates a JWT token against user details.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to validate against
     * @return true if token is valid, false otherwise
     */
    Boolean validateToken(String token, UserDetails userDetails);

    /**
     * Checks if a JWT token is expired.
     *
     * @param token the JWT token to check
     * @return true if token is expired, false otherwise
     */
    Boolean isExpired(String token);

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the expiration date of the token
     */
    Date extractExpiration(String token);
}

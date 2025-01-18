package com.marketplace.auth.service.impl;

import com.marketplace.api.dto.UserDTO;
import com.marketplace.api.dto.auth.JwtResponse;
import com.marketplace.api.service.UserService;
import com.marketplace.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final UserService userService;

    @Value("${app.security.access-token.access-token-expiration}")
    private final long accessTokenExpiration = 15 * 60 * 1000;
    @Value("${app.security.access-token.refresh-token-expiration}")
    private final long refreshAccessTokenExpiration = 24 * 60 * 60 * 1000;

    @Value("${app.security.access-token.secret-key}")
    private String secretKey;

    /**
     * Generates both access and refresh JWT tokens for a user
     *
     * @param user User information to include in tokens
     * @return Object containing access and refresh tokens
     */
    @Override
    public JwtResponse generateJwtToken(UserDTO user) {
        return new JwtResponse(
                generateAccessToken(user),
                generateRefreshToken(user)
        );
    }

    /**
     * Extracts the subject (user ID) from a JWT token
     *
     * @param token JWT token to parse
     * @return Subject value from token claims
     */
    @Override
    public String extractSubject(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    /**
     * Extracts a specific claim value from a JWT token
     *
     * @param token JWT token to parse
     * @param claimName Name of the claim to extract
     * @return Value of the specified claim
     */
    @Override
    public Object extractClaimValue(String token, String claimName) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimName);
    }

    /**
     * Validates a JWT token against user details
     *
     * @param token JWT token to validate
     * @param userDetails User details to validate against
     * @return True if token is valid, false otherwise
     */
    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        if (isExpired(token)) {
            return false;
        }

        Long userId = Long.parseLong(extractSubject(token));
        UserDTO user = userService.getUserById(userId);
        return user.email().equals(userDetails.getUsername());
    }

    /**
     * Checks if a JWT token is expired
     *
     * @param token JWT token to check
     * @return True if token is expired, false otherwise
     */
    @Override
    public Boolean isExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Extracts expiration date from a JWT token
     *
     * @param token JWT token to parse
     * @return Expiration date of the token
     */
    @Override
    public Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    /**
     * Generates an access token with default claims
     *
     * @param user User to generate token for
     * @return Generated access token
     */
    private String generateAccessToken(UserDTO user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("typ", "Bearer");
        return generateAccessToken(user, claims);
    }

    /**
     * Generates an access token with custom claims
     *
     * @param user User to generate token for
     * @param extraClaims Additional claims to include in token
     * @return Generated access token
     */
    private String generateAccessToken(UserDTO user, Map<String, Object> extraClaims) {
        return buildToken(extraClaims, user,  accessTokenExpiration);
    }

    /**
     * Generates a refresh token with default claims
     *
     * @param userDetails User to generate token for
     * @return Generated refresh token
     */
    private String generateRefreshToken(UserDTO userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("typ", "Refresh");
        return generateRefreshToken(userDetails, claims);
    }

    /**
     * Generates a refresh token with custom claims
     *
     * @param userDetails User to generate token for
     * @param extraClaims Additional claims to include in token
     * @return Generated refresh token
     */
    private String generateRefreshToken(UserDTO userDetails, Map<String, Object> extraClaims) {
        return buildToken(extraClaims, userDetails, refreshAccessTokenExpiration);
    }

    /**
     * Builds a JWT token with specified claims and expiration
     *
     * @param extraClaims Claims to include in token
     * @param userDetails User details for token
     * @param expiration Token expiration time in milliseconds
     * @return Built JWT token string
     */
    private String buildToken(Map<String, Object> extraClaims, UserDTO userDetails, long expiration) {
        return Jwts.builder()
                .subject(Long.toString(userDetails.id()))
                .claims(extraClaims)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * Extracts all claims from a JWT token
     *
     * @param token JWT token to parse
     * @return Claims object containing all token claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Gets the secret key for JWT signing/verification
     *
     * @return SecretKey instance for JWT operations
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}

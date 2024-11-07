package com.marketplace.security.jwt;

import com.marketplace.dto.JwtResponse;
import com.marketplace.entity.User;
import com.marketplace.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtService jwtService;

    private User testUser;
    private final String TEST_SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        // Setup secretKey private field to set
        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET_KEY);

        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void generateJwtToken_ShouldReturnValidJwtResponse() {
        JwtResponse response = jwtService.generateJwtToken(testUser);

        assertNotNull(response);
        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());

        assertNotEquals(response.accessToken(), response.refreshToken());

        assertEquals("Bearer", jwtService.extractClaimValue(response.accessToken(), "typ"));
        assertEquals("Refresh", jwtService.extractClaimValue(response.refreshToken(), "typ"));
    }

    @Test
    void generateAccessToken_ShouldCreateValidToken() {
        String token = jwtService.generateAccessToken(testUser);

        assertNotNull(token);
        assertEquals(String.valueOf(testUser.getId()), jwtService.extractSubject(token));
        assertEquals("Bearer", jwtService.extractClaimValue(token, "typ"));
        assertFalse(jwtService.isExpired(token));
    }

    @Test
    void generateAccessToken_WithExtraClaims_ShouldIncludeClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("custom", "value");
        extraClaims.put("typ", "Bearer");

        String token = jwtService.generateAccessToken(testUser, extraClaims);

        assertNotNull(token);
        assertEquals("value", jwtService.extractClaimValue(token, "custom"));
        assertEquals("Bearer", jwtService.extractClaimValue(token, "typ"));
    }

    @Test
    void generateRefreshToken_ShouldCreateValidToken() {
        String token = jwtService.generateRefreshToken(testUser);

        assertNotNull(token);
        assertEquals(String.valueOf(testUser.getId()), jwtService.extractSubject(token));
        assertEquals("Refresh", jwtService.extractClaimValue(token, "typ"));
        assertFalse(jwtService.isExpired(token));
    }

    @Test
    void buildToken_ShouldCreateValidToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("test", "value");
        long expiration = 3600000; // 1 hour

        String token = jwtService.buildToken(claims, testUser, expiration);

        assertNotNull(token);
        Claims extractedClaims = jwtService.extractAllClaims(token);
        assertEquals(String.valueOf(testUser.getId()), extractedClaims.getSubject());
        assertEquals("value", extractedClaims.get("test"));
        assertTrue(extractedClaims.getExpiration().after(new Date()));
    }

    @Test
    void extractSubject_ShouldReturnCorrectSubject() {
        String token = jwtService.generateAccessToken(testUser);

        String subject = jwtService.extractSubject(token);

        assertEquals(String.valueOf(testUser.getId()), subject);
    }

    @Test
    void extractAllClaims_ShouldReturnAllClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("custom", "value");
        extraClaims.put("typ", "Bearer");
        String token = jwtService.generateAccessToken(testUser, extraClaims);

        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals(String.valueOf(testUser.getId()), claims.getSubject());
        assertEquals("value", claims.get("custom"));
        assertEquals("Bearer", claims.get("typ"));
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        String token = jwtService.generateAccessToken(testUser);
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        boolean isValid = jwtService.validateToken(token, testUser);

        assertTrue(isValid);
    }

    @Test
    void validateToken_WithExpiredToken_ShouldReturnFalse() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("typ", "Bearer");
        String token = jwtService.buildToken(claims, testUser, -3600000);

        boolean isValid = jwtService.validateToken(token, testUser);

        assertFalse(isValid);
    }

    @Test
    void validateToken_WithNonExistentUser_ShouldThrowException() {
        String token = jwtService.generateAccessToken(testUser);
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                jwtService.validateToken(token, testUser)
        );
    }

    @Test
    void isExpired_WithValidToken_ShouldReturnFalse() {
        // Arrange
        String token = jwtService.generateAccessToken(testUser);

        // Act & Assert
        assertFalse(jwtService.isExpired(token));
    }

    @Test
    void isExpired_WithExpiredToken_ShouldReturnTrue() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("typ", "Bearer");
        String token = jwtService.buildToken(claims, testUser, -3600000); // Token expiré

        assertTrue(jwtService.isExpired(token));
    }

    @Test
    void extractExpiration_ShouldReturnCorrectDate() {
        long currentTime = System.currentTimeMillis();
        String token = jwtService.generateAccessToken(testUser);

        Date expiration = jwtService.extractExpiration(token);

        assertTrue(expiration.after(new Date(currentTime)));
        assertTrue(expiration.before(new Date(currentTime + 16 * 60 * 1000))); // 15 minutes + marge
    }

    @Test
    void extractClaimValue_ShouldReturnCorrectValue() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("custom", "value");
        String token = jwtService.generateAccessToken(testUser, extraClaims);

        Object claimValue = jwtService.extractClaimValue(token, "custom");

        assertEquals("value", claimValue);
    }
}
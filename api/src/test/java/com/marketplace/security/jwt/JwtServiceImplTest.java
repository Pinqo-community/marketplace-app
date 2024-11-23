package com.marketplace.security.jwt;

import com.marketplace.dto.JwtResponse;
import com.marketplace.entity.User;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtServiceImpl jwtService;

    private User testUser;

    private final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        // Set up the private secretKey field to store the secret key
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);

        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Nested
    class GenerateJwtToken {
        @Test
        void whenValidUser_thenJwtResponse() {
            // When
            JwtResponse response = jwtService.generateJwtToken(testUser);

            // Then
            assertNotNull(response);
            assertNotNull(response.accessToken());
            assertNotNull(response.refreshToken());

            assertNotEquals(response.accessToken(), response.refreshToken());

            assertEquals("Bearer", jwtService.extractClaimValue(response.accessToken(), "typ"));
            assertEquals("Refresh", jwtService.extractClaimValue(response.refreshToken(), "typ"));
        }
    }

    @Nested
    class ExtractSubject {
        @Test
        void whenValidToken_thenReturnSubject() {
            // Given
            JwtResponse token = jwtService.generateJwtToken(testUser);

            // When
            String result = jwtService.extractSubject(token.accessToken());

            // Then
            assertNotNull(result);
            assertEquals(String.valueOf(testUser.getId()), result);
        }
    }

    @Nested
    class ValidateToken {
        @Test
        void whenValidToken_thenReturnTrue() {
            // Given
            JwtResponse token = jwtService.generateJwtToken(testUser);
            when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

            // When
            boolean result = jwtService.validateToken(token.accessToken(), testUser);

            // Then
            assertTrue(result);
        }

        @Test
        void whenExpiredToken_thenReturnFalse() {
            // Given
            SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));;

            String expiredToken = Jwts.builder()
                    .subject("testUser")
                    .issuedAt(new Date(System.currentTimeMillis() - 3600000)) // Issued 1 hour ago
                    .expiration(new Date(System.currentTimeMillis() - 1800000)) // Expired 30 mins ago
                    .signWith(secretKey)
                    .compact();

            // When
            Boolean result = jwtService.validateToken(expiredToken, testUser);

            // Then
            assertFalse(result);
        }
    }

    @Nested
    class IsExpired {
        @Test
        void whenValidToken_thenReturnFalse() {
            // Given
            JwtResponse token = jwtService.generateJwtToken(testUser);

            // When
            boolean result = jwtService.isExpired(token.accessToken());

            // Then
            assertFalse(result);
        }

        @Test
        void whenExpiredToken_thenReturnTrue() {
            // Given
            SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));;

            String expiredToken = Jwts.builder()
                    .subject("testUser")
                    .issuedAt(new Date(System.currentTimeMillis() - 3600000)) // Issued 1 hour ago
                    .expiration(new Date(System.currentTimeMillis() - 1800000)) // Expired 30 mins ago
                    .signWith(secretKey)
                    .compact();

            // When
            Boolean result = jwtService.isExpired(expiredToken);

            // Then
            assertTrue(result);
        }
    }

    @Nested
    class ExtractExpiration {
        @Test
        void whenValidToken_thenReturnDate() {
            // Given
            JwtResponse token = jwtService.generateJwtToken(testUser);

            // When
            boolean result = jwtService.isExpired(token.accessToken());

            // Then
            assertFalse(result);
        }
    }
}
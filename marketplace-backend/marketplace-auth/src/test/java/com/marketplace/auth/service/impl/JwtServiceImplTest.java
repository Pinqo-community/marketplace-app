package com.marketplace.auth.service.impl;

import com.marketplace.api.dto.auth.JwtResponse;
import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.service.UserService;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        // Injecter manuellement la clé secrète car elle vient de @Value
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
    }

    private UserDTO createMockUserDTO() {
        return new UserDTO(
                1L,
                "test@test.com",
                "encodedPassword",
                true,
                Set.of("ROLE_USER"),
                "local"
        );
    }

    @Nested
    class GenerateJwtToken {
        @Test
        void shouldGenerateValidTokens() {
            // Given
            UserDTO user = createMockUserDTO();

            // When
            JwtResponse response = jwtService.generateJwtToken(user);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.accessToken()).isNotEmpty();
            assertThat(response.refreshToken()).isNotEmpty();

            // Vérifier que les tokens contiennent les bonnes informations
            assertThat(jwtService.extractSubject(response.accessToken()))
                    .isEqualTo(user.id().toString());
            assertThat(jwtService.extractClaimValue(response.accessToken(), "typ"))
                    .isEqualTo("Bearer");
            assertThat(jwtService.extractClaimValue(response.refreshToken(), "typ"))
                    .isEqualTo("Refresh");
        }
    }

    @Nested
    class ExtractSubject {
        @Test
        void shouldExtractSubjectFromValidToken() {
            // Given
            UserDTO user = createMockUserDTO();
            String token = jwtService.generateJwtToken(user).accessToken();

            // When
            String subject = jwtService.extractSubject(token);

            // Then
            assertThat(subject).isEqualTo(user.id().toString());
        }

        @Test
        void shouldThrowExceptionForInvalidToken() {
            // Given
            String invalidToken = "invalidToken";

            // When & Then
            assertThatThrownBy(() -> jwtService.extractSubject(invalidToken))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    class ExtractClaimValue {
        @Test
        void shouldExtractClaimFromValidToken() {
            // Given
            UserDTO user = createMockUserDTO();
            String token = jwtService.generateJwtToken(user).accessToken();

            // When
            Object claim = jwtService.extractClaimValue(token, "typ");

            // Then
            assertThat(claim).isEqualTo("Bearer");
        }

        @Test
        void shouldReturnNullForNonExistentClaim() {
            // Given
            UserDTO user = createMockUserDTO();
            String token = jwtService.generateJwtToken(user).accessToken();

            // When
            Object claim = jwtService.extractClaimValue(token, "nonexistent");

            // Then
            assertThat(claim).isNull();
        }
    }

    @Nested
    class ValidateToken {
        @Test
        void shouldReturnTrueForValidToken() {
            // Given
            UserDTO user = createMockUserDTO();
            String token = jwtService.generateJwtToken(user).accessToken();
            UserDetails userDetails = User.builder()
                    .username(user.email())
                    .password(user.password())
                    .authorities(Collections.emptyList())
                    .build();

            when(userService.getUserById(user.id())).thenReturn(user);

            // When
            boolean isValid = jwtService.validateToken(token, userDetails);

            // Then
            assertThat(isValid).isTrue();
        }

        @Test
        void shouldReturnFalseForDifferentUser() {
            // Given
            UserDTO user = createMockUserDTO();
            String token = jwtService.generateJwtToken(user).accessToken();
            UserDetails differentUserDetails = User.builder()
                    .username("different@email.com")
                    .password("password")
                    .authorities(Collections.emptyList())
                    .build();

            when(userService.getUserById(user.id())).thenReturn(user);

            // When
            boolean isValid = jwtService.validateToken(token, differentUserDetails);

            // Then
            assertThat(isValid).isFalse();
        }
    }

    @Nested
    class IsExpired {
        @Test
        void shouldReturnTrueForExpiredToken() throws Exception {
            // Given
            UserDTO user = createMockUserDTO();
            String token = createExpiredToken(user);

            // When & Then
            assertThat(jwtService.isExpired(token)).isTrue();
        }

        @Test
        void shouldReturnFalseForValidToken() {
            // Given
            UserDTO user = createMockUserDTO();
            String token = jwtService.generateJwtToken(user).accessToken();

            // When & Then
            assertThat(jwtService.isExpired(token)).isFalse();
        }

        private String createExpiredToken(UserDTO user) {
            return Jwts.builder()
                    .subject(user.id().toString())
                    .expiration(new Date(System.currentTimeMillis() - 1000))
                    .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970")))
                    .compact();
        }
    }

    @Nested
    class ExtractExpiration {
        @Test
        void shouldExtractExpirationFromValidToken() {
            // Given
            UserDTO user = createMockUserDTO();
            String token = jwtService.generateJwtToken(user).accessToken();

            // When
            Date expiration = jwtService.extractExpiration(token);

            // Then
            assertThat(expiration)
                    .isNotNull()
                    .isAfter(new Date());
        }

        @Test
        void shouldThrowExceptionForInvalidToken() {
            // Given
            String invalidToken = "invalidToken";

            // When & Then
            assertThatThrownBy(() -> jwtService.extractExpiration(invalidToken))
                    .isInstanceOf(RuntimeException.class);
        }
    }
}
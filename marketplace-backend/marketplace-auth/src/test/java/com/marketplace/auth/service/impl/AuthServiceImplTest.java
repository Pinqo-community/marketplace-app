package com.marketplace.auth.service.impl;

import com.marketplace.api.dto.auth.JwtResponse;
import com.marketplace.api.dto.auth.LoginRequest;
import com.marketplace.api.dto.auth.RegisterRequest;
import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.exception.InvalidTokenException;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.api.exception.WrongCredentialException;
import com.marketplace.api.service.UserService;
import com.marketplace.auth.entity.InvalidRefreshToken;
import com.marketplace.auth.model.BasicUserInfos;
import com.marketplace.auth.repository.InvalidRefreshTokenRepository;
import com.marketplace.auth.service.JwtService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private InvalidRefreshTokenRepository invalidRefreshTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

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
    class Register {
        @Test
        void whenValidRequest_thenSuccessfulRegistration() {
            // Given
            RegisterRequest request = new RegisterRequest(
                    "test@test.com",
                    "password",
                    "John",
                    "Doe"
            );
            String encodedPassword = "encodedPassword";
            UserDTO userDTO = createMockUserDTO();
            JwtResponse expectedResponse = new JwtResponse("accessToken", "refreshToken");

            when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
            when(userService.createUser(any(BasicUserInfos.class))).thenReturn(userDTO);
            when(jwtService.generateJwtToken(userDTO)).thenReturn(expectedResponse);

            // When
            JwtResponse actualResponse = authService.register(request);

            // Then
            assertThat(actualResponse).isEqualTo(expectedResponse);
            verify(userService).createUser(any(BasicUserInfos.class));
            verify(jwtService).generateJwtToken(userDTO);
        }
    }

    @Nested
    class Authenticate {
        @Test
        void whenValidCredentials_thenSuccessfulAuthentication() {
            // Given
            LoginRequest request = new LoginRequest("test@test.com", "password");
            UserDTO userDTO = createMockUserDTO();
            JwtResponse expectedResponse = new JwtResponse("accessToken", "refreshToken");

            when(userService.getUserByEmail(request.email())).thenReturn(userDTO);
            when(jwtService.generateJwtToken(userDTO)).thenReturn(expectedResponse);

            // When
            JwtResponse actualResponse = authService.authenticate(request);

            // Then
            assertThat(actualResponse).isEqualTo(expectedResponse);
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService).generateJwtToken(userDTO);
        }

        @Test
        void whenNonLocalProvider_thenThrowWrongCredentialException() {
            // Given
            LoginRequest request = new LoginRequest("test@test.com", "password");
            UserDTO userDTO = new UserDTO(
                    1L,
                    "test@test.com",
                    "encodedPassword",
                    true,
                    Set.of("ROLE_USER"),
                    "google"  // Provider différent de "local"
            );

            when(userService.getUserByEmail(request.email())).thenReturn(userDTO);

            // When & Then
            assertThatThrownBy(() -> authService.authenticate(request))
                    .isInstanceOf(WrongCredentialException.class)
                    .hasMessage("Les identifiants sont invalides");
        }

        @Test
        void whenAuthenticationFails_thenThrowWrongCredentialException() {
            // Given
            LoginRequest request = new LoginRequest("test@test.com", "password");
            UserDTO userDTO = createMockUserDTO();

            when(userService.getUserByEmail(request.email())).thenReturn(userDTO);
            when(authenticationManager.authenticate(any()))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            // When & Then
            assertThatThrownBy(() -> authService.authenticate(request))
                    .isInstanceOf(WrongCredentialException.class)
                    .hasMessage("Les identifiants sont invalides");
        }

        @Test
        void whenUserNotFound_thenThrowWrongCredentialException() {
            // Given
            LoginRequest request = new LoginRequest("test@test.com", "password");

            when(userService.getUserByEmail(request.email()))
                    .thenThrow(new NotFoundException("User not found"));

            // When & Then
            assertThatThrownBy(() -> authService.authenticate(request))
                    .isInstanceOf(WrongCredentialException.class)
                    .hasMessage("Les identifiants sont invalides");
        }
    }

    @Nested
    class RefreshToken {
        @Test
        void whenValidRefreshToken_thenSuccessfulTokenRefresh() {
            // Given
            String refreshToken = "validRefreshToken";
            Long userId = 1L;
            UserDTO userDTO = createMockUserDTO();
            JwtResponse expectedResponse = new JwtResponse("newAccessToken", "newRefreshToken");
            Date expirationDate = new Date();

            when(jwtService.isExpired(refreshToken)).thenReturn(false);
            when(jwtService.extractClaimValue(refreshToken, "typ")).thenReturn("Refresh");
            when(invalidRefreshTokenRepository.existsByToken(refreshToken)).thenReturn(false);
            when(jwtService.extractSubject(refreshToken)).thenReturn(userId.toString());
            when(jwtService.extractExpiration(refreshToken)).thenReturn(expirationDate);
            when(userService.getUserById(userId)).thenReturn(userDTO);
            when(jwtService.generateJwtToken(userDTO)).thenReturn(expectedResponse);

            // When
            JwtResponse actualResponse = authService.refreshToken(refreshToken);

            // Then
            assertThat(actualResponse).isEqualTo(expectedResponse);
            verify(invalidRefreshTokenRepository).save(any(InvalidRefreshToken.class));
        }

        @Test
        void whenExpiredToken_thenThrowInvalidTokenException() {
            // Given
            String refreshToken = "expiredToken";
            when(jwtService.isExpired(refreshToken)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("Token invalide");
        }

        @Test
        void whenInvalidTokenType_thenThrowInvalidTokenException() {
            // Given
            String refreshToken = "invalidTypeToken";
            when(jwtService.isExpired(refreshToken)).thenReturn(false);
            when(jwtService.extractClaimValue(refreshToken, "typ")).thenReturn("Access");

            // When & Then
            assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("Token invalide");
        }

        @Test
        void whenTokenBlacklisted_thenThrowInvalidTokenException() {
            // Given
            String refreshToken = "blacklistedToken";
            when(jwtService.isExpired(refreshToken)).thenReturn(false);
            when(jwtService.extractClaimValue(refreshToken, "typ")).thenReturn("Refresh");
            when(invalidRefreshTokenRepository.existsByToken(refreshToken)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("Token invalide");
        }

        @Test
        void whenUserNotFound_thenThrowInvalidTokenException() {
            // Given
            String refreshToken = "validRefreshToken";
            Long userId = 1L;

            when(jwtService.isExpired(refreshToken)).thenReturn(false);
            when(jwtService.extractClaimValue(refreshToken, "typ")).thenReturn("Refresh");
            when(invalidRefreshTokenRepository.existsByToken(refreshToken)).thenReturn(false);
            when(jwtService.extractSubject(refreshToken)).thenReturn(userId.toString());
            when(userService.getUserById(userId)).thenThrow(new NotFoundException("User not found"));

            // When & Then
            assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("Token invalide");
        }
    }
}
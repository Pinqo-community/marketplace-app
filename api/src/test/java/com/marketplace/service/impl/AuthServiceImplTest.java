package com.marketplace.service.impl;

import com.marketplace.dto.LoginRequest;
import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.InvalidRefreshToken;
import com.marketplace.entity.User;
import com.marketplace.exception.InvalidTokenException;
import com.marketplace.exception.WrongCredentialException;
import com.marketplace.model.user.BasicUserInfos;
import com.marketplace.repository.InvalidRefreshTokenRepository;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.JwtService;
import com.marketplace.service.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private InvalidRefreshTokenRepository invalidRefreshTokenRepository;

    @InjectMocks
    private AuthServiceImpl authenticationService;

    @Nested
    class Register {
        @Test
        void whenValidUser_thenReturnUser() {
            // Given
            RegisterRequest request = new RegisterRequest("test@email.com", "password", "John", "Doe");
            User user = mock(User.class);

            when(userService.createUser(any(BasicUserInfos.class))).thenReturn(user);

            // When
            User result = authenticationService.register(request);

            // Then
            assertNotNull(result);
            assertEquals(user, result);
            verify(userService).createUser(any(BasicUserInfos.class));
        }
    }

    @Nested
    class Authenticate {
        @Test
        void whenValidCredentials_thenAuthenticateUser() {
            // Given
            LoginRequest request = new LoginRequest("test@email.com", "password");
            User user = mock(User.class);
            Authentication authentication = mock(Authentication.class);

            when(user.isLocalProviderAuthentication()).thenReturn(true);
            when(userRepository.findByEmail(request.email()))
                    .thenReturn(Optional.of(user));
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

            // When
            User result = authenticationService.authenticate(request);

            // Then
            assertNotNull(result);
            assertEquals(user, result);
            verify(authenticationManager).authenticate(
                    any(UsernamePasswordAuthenticationToken.class)
            );
        }

        @Test
        void whenEmailNotExists_thenThrowException() {
            // Given
            LoginRequest request = new LoginRequest("wrong@email.com", "password");

            when(userRepository.findByEmail(request.email()))
                    .thenThrow(new WrongCredentialException("Les identifiants sont invalides"));

            // When
            WrongCredentialException result = assertThrows(WrongCredentialException.class, () -> authenticationService.authenticate(request));

            // Then
            assertEquals("Les identifiants sont invalides", result.getMessage());
        }

        @Test
        void whenProviderNotLocal_thenThrowException() {
            // Given
            LoginRequest request = new LoginRequest("test@email.com", "password");
            User user = mock(User.class);

            when(userRepository.findByEmail(request.email()))
                    .thenReturn(Optional.of(user));
            when(user.isLocalProviderAuthentication()).thenReturn(false);

            // When
            WrongCredentialException result = assertThrows(WrongCredentialException.class, () -> authenticationService.authenticate(request));

            // Then
            assertEquals("Les identifiants sont invalides", result.getMessage());
        }

        @Test
        void whenWrongCredentials_thenThrowException() {
            // Given
            LoginRequest request = new LoginRequest("test@email.com", "password");
            User user = mock(User.class);

            when(user.isLocalProviderAuthentication()).thenReturn(true);
            when(userRepository.findByEmail(request.email()))
                    .thenReturn(Optional.of(user));
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Invalid credentials"));

            // When
            WrongCredentialException result = assertThrows(WrongCredentialException.class, () -> authenticationService.authenticate(request));

            // Then
            assertEquals("Les identifiants sont invalides", result.getMessage());
        }
    }

    @Nested
    class RefreshToken {
        @Test
        void whenValidToken_thenReturnUser() {
            // Given
            String refreshToken = "valid-refresh-token";
            User user = mock(User.class);
            InvalidRefreshToken invalidRefreshToken = mock(InvalidRefreshToken.class);

            when(jwtService.isExpired(any(String.class))).thenReturn(false);
            when(jwtService.extractClaimValue(any(String.class), any(String.class))).thenReturn("Refresh");
            when(invalidRefreshTokenRepository.existsByToken(any(String.class))).thenReturn(false);

            when(invalidRefreshTokenRepository.save(any())).thenReturn(invalidRefreshToken);

            when(jwtService.extractSubject(refreshToken)).thenReturn("1");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            // When
            User result = authenticationService.refreshToken(refreshToken);

            // Then
            assertNotNull(result);
            assertEquals(user, result);
        }

        @Test
        void whenExpiredToken_thenThrowException() {
            // Given
            String refreshToken = "valid-refresh-token";

            when(jwtService.isExpired(any(String.class))).thenReturn(true);

            // When
            InvalidTokenException result = assertThrows(InvalidTokenException.class, () -> authenticationService.refreshToken(refreshToken));

            // Then
            assertEquals("Token invalide", result.getMessage());
        }

        @Test
        void whenNotRefreshToken_thenThrowException() {
            // Given
            String refreshToken = "valid-refresh-token";

            when(jwtService.isExpired(any(String.class))).thenReturn(false);
            when(jwtService.extractClaimValue(any(String.class), any(String.class))).thenReturn("Access");

            // When
            InvalidTokenException result = assertThrows(InvalidTokenException.class, () -> authenticationService.refreshToken(refreshToken));

            // Then
            assertEquals("Token invalide", result.getMessage());
        }

        @Test
        void whenAlreadyUseRefreshToken_thenThrowException() {
            // Given
            String refreshToken = "valid-refresh-token";

            when(jwtService.isExpired(any(String.class))).thenReturn(false);
            when(jwtService.extractClaimValue(any(String.class), any(String.class))).thenReturn("Refresh");
            when(invalidRefreshTokenRepository.existsByToken(any(String.class))).thenReturn(true);

            // When
            InvalidTokenException result = assertThrows(InvalidTokenException.class, () -> authenticationService.refreshToken(refreshToken));

            // Then
            assertEquals("Token invalide", result.getMessage());
        }

        @Test
        void whenWrongUserInToken_thenThrowException() {
            // Given
            String refreshToken = "valid-refresh-token";
            User user = mock(User.class);
            InvalidRefreshToken invalidRefreshToken = mock(InvalidRefreshToken.class);

            when(jwtService.isExpired(any(String.class))).thenReturn(false);
            when(jwtService.extractClaimValue(any(String.class), any(String.class))).thenReturn("Refresh");
            when(invalidRefreshTokenRepository.existsByToken(any(String.class))).thenReturn(false);

            when(invalidRefreshTokenRepository.save(any())).thenReturn(invalidRefreshToken);

            when(jwtService.extractSubject(refreshToken)).thenReturn("1");
            when(userRepository.findById(1L)).thenThrow(new InvalidTokenException("Token invalide"));

            // When
            InvalidTokenException result = assertThrows(InvalidTokenException.class, () -> authenticationService.refreshToken(refreshToken));

            // Then
            assertEquals("Token invalide", result.getMessage());
        }
    }
}
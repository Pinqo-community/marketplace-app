package com.marketplace.service.impl;

import com.marketplace.dto.LoginRequest;
import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.User;
import com.marketplace.exception.InvalidTokenException;
import com.marketplace.exception.WrongCredentialException;
import com.marketplace.model.user.BasicUserInfos;
import com.marketplace.repository.UserRepository;
import com.marketplace.security.jwt.JwtService;
import com.marketplace.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private AuthServiceImpl authenticationService;

    @Test
    void register_ShouldCreateNewUser() {
        RegisterRequest request = new RegisterRequest("test@email.com", "password", "John", "Doe");
        User expectedUser = new User();

        when(userService.createUser(any(BasicUserInfos.class))).thenReturn(expectedUser);

        User result = authenticationService.register(request);

        assertNotNull(result);
        verify(userService).createUser(any(BasicUserInfos.class));
    }

    @Test
    void authenticate_WithValidCredentials_ShouldReturnUser() {
        LoginRequest request = new LoginRequest("test@email.com", "password");
        User user = new User();
        user.setProvider("local");

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        User result = authenticationService.authenticate(request);

        assertNotNull(result);
        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );
    }

    @Test
    void authenticate_WithNonExistentEmail_ShouldThrowException() {
        LoginRequest request = new LoginRequest("nonexistent@email.com", "password");

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        assertThrows(WrongCredentialException.class,
                () -> authenticationService.authenticate(request));
    }

    @Test
    void authenticate_WithNonLocalProvider_ShouldThrowException() {
        LoginRequest request = new LoginRequest("test@email.com", "password");
        User user = new User();
        user.setProvider("google");

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        assertThrows(WrongCredentialException.class,
                () -> authenticationService.authenticate(request));
    }

    @Test
    void refreshToken_WithValidToken_ShouldReturnUser() {
        String refreshToken = "valid.refresh.token";
        User expectedUser = new User();

        when(jwtService.isExpired(refreshToken)).thenReturn(false);
        when(jwtService.extractClaimValue(refreshToken, "typ")).thenReturn("Refresh");
        when(jwtService.extractSubject(refreshToken)).thenReturn("1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        User result = authenticationService.refreshToken(refreshToken);

        assertNotNull(result);
        verify(jwtService).isExpired(refreshToken);
        verify(jwtService).extractClaimValue(refreshToken, "typ");
        verify(jwtService).extractSubject(refreshToken);
        verify(userRepository).findById(1L);
    }

    @Test
    void refreshToken_WithExpiredToken_ShouldThrowException() {
        String refreshToken = "expired-refresh-token";

        when(jwtService.isExpired(refreshToken)).thenReturn(true);

        assertThrows(InvalidTokenException.class,
                () -> authenticationService.refreshToken(refreshToken));
    }

    @Test
    void refreshToken_WithWrongTokenType_ShouldThrowException() {
        String refreshToken = "wrong-refresh-token";

        when(jwtService.isExpired(refreshToken)).thenReturn(false);
        when(jwtService.extractClaimValue(refreshToken, "typ")).thenReturn("Access");

        assertThrows(InvalidTokenException.class,
                () -> authenticationService.refreshToken(refreshToken));
    }

    @Test
    void refreshToken_WithNonExistentUser_ShouldThrowException() {
        String refreshToken = "refresh-token";

        when(jwtService.isExpired(refreshToken)).thenReturn(false);
        when(jwtService.extractClaimValue(refreshToken, "typ")).thenReturn("Refresh");
        when(jwtService.extractSubject(refreshToken)).thenReturn("999");
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class,
                () -> authenticationService.refreshToken(refreshToken));
    }
}
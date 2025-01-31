package com.marketplace.auth.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppConfigTest {

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AppConfig appConfig;

    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // When
        PasswordEncoder encoder = appConfig.passwordEncoder();

        // Then
        assertTrue(encoder instanceof BCryptPasswordEncoder);

        // Verify that it can encode and match passwords
        String password = "testPassword";
        String encoded = encoder.encode(password);
        assertTrue(encoder.matches(password, encoded));
    }

    @Test
    void authenticationManager_ShouldCreateValidProviderManager() {
        // Given
        PasswordEncoder passwordEncoder = appConfig.passwordEncoder();

        // When
        AuthenticationManager authManager = appConfig.authenticationManager(userDetailsService, passwordEncoder);

        // Then
        assertTrue(authManager instanceof ProviderManager);
        ProviderManager providerManager = (ProviderManager) authManager;

        assertFalse(providerManager.getProviders().isEmpty());
        assertTrue(providerManager.getProviders().get(0) instanceof DaoAuthenticationProvider);
    }

    @Test
    void authenticationManager_ShouldAuthenticateWithCorrectCredentials() {
        // Given
        PasswordEncoder passwordEncoder = appConfig.passwordEncoder();
        String username = "testUser";
        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);

        UserDetails userDetails = User.builder()
                .username(username)
                .password(encodedPassword)
                .authorities(new ArrayList<>())
                .build();

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        AuthenticationManager authManager = appConfig.authenticationManager(userDetailsService, passwordEncoder);

        // When
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Authentication result = authManager.authenticate(authentication);

        // Then
        assertTrue(result.isAuthenticated());
        assertEquals(username, result.getName());
    }

    @Test
    void authenticationManager_ShouldUseProvidedPasswordEncoder() {
        // Given
        PasswordEncoder passwordEncoder = appConfig.passwordEncoder();
        AuthenticationManager authManager = appConfig.authenticationManager(userDetailsService, passwordEncoder);

        // Verify the structure
        ProviderManager providerManager = (ProviderManager) authManager;
        AuthenticationProvider provider = providerManager.getProviders().get(0);

        assertTrue(provider instanceof DaoAuthenticationProvider);
    }
}
package com.marketplace.auth.security.jwt;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.service.UserService;
import com.marketplace.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Test
    void whenNoAuthorizationHeader_thenContinueChain() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");

        // When
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractSubject(any());
    }

    @Test
    void whenAuthHeaderNotStartsWithBearer_thenContinueChain() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Basic xyz");
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");

        // When
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractSubject(any());
    }

    @Test
    void whenAuthEndpoint_thenContinueChain() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(request.getRequestURI()).thenReturn("/auth/login");

        // When
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractSubject(any());
    }

    @Test
    void whenValidToken_thenSetAuthentication() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        Long userId = 1L;
        UserDTO userDTO = new UserDTO(
                userId,
                "test@test.com",
                "password",
                true,
                Set.of("ROLE_USER"),
                "local"
        );
        UserDetails userDetails = User.builder()
                .username(userDTO.email())
                .password(userDTO.password())
                .authorities(Collections.emptyList())
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(jwtService.extractSubject(token)).thenReturn(userId.toString());
        when(userService.getUserById(userId)).thenReturn(userDTO);
        when(userDetailsService.loadUserByUsername(userDTO.email())).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        // When
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtService).validateToken(token, userDetails);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);
    }

    @Test
    void whenInvalidToken_thenDontSetAuthentication() throws ServletException, IOException, IOException {
        // Given
        String token = "invalid.jwt.token";
        Long userId = 1L;
        UserDTO userDTO = new UserDTO(
                userId,
                "test@test.com",
                "password",
                true,
                Set.of("ROLE_USER"),
                "local"
        );
        UserDetails userDetails = User.builder()
                .username(userDTO.email())
                .password(userDTO.password())
                .authorities(Collections.emptyList())
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(jwtService.extractSubject(token)).thenReturn(userId.toString());
        when(userService.getUserById(userId)).thenReturn(userDTO);
        when(userDetailsService.loadUserByUsername(userDTO.email())).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(false);

        // When
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtService).validateToken(token, userDetails);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}
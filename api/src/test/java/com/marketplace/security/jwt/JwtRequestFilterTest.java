package com.marketplace.security.jwt;

import com.marketplace.entity.User;
import com.marketplace.exception.InvalidTokenException;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.JwtService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private User mockUser;
    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();

        response = new MockHttpServletResponse();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        mockUserDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@example.com")
                .password("password")
                .authorities("ROLE_USER")
                .build();
    }

    @Test
    void shouldSkipFilterWhenNoAuthorizationHeader() throws Exception {
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void shouldSkipFilterWhenAuthHeaderDoesNotStartWithBearer() throws Exception {
        request.addHeader("Authorization", "xyz");

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void shouldSkipFilterForAuthEndpoints() throws Exception {
        request.setRequestURI("/auth/login");
        request.addHeader("Authorization", "Bearer token");

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void shouldSetAuthenticationWhenValidToken() throws Exception {
        request.addHeader("Authorization", "Bearer valid_token");
        when(jwtService.extractSubject("valid_token")).thenReturn("1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(mockUserDetails);
        when(jwtService.validateToken("valid_token", mockUserDetails)).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).validateToken("valid_token", mockUserDetails);
        verify(userDetailsService).loadUserByUsername("test@example.com");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        request.addHeader("Authorization", "Bearer valid_token");
        when(jwtService.extractSubject("valid_token")).thenReturn("1");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class, () -> {
            jwtRequestFilter.doFilterInternal(request, response, filterChain);
        });
    }

    @Test
    void shouldNotSetAuthenticationWhenInvalidToken() throws Exception {
        request.addHeader("Authorization", "Bearer invalid_token");
        when(jwtService.extractSubject("invalid_token")).thenReturn("1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(mockUserDetails);
        when(jwtService.validateToken("invalid_token", mockUserDetails)).thenReturn(false);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).validateToken("invalid_token", mockUserDetails);
    }
}
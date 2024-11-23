package com.marketplace.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AuthenticationException authException;

    @InjectMocks
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void commence_WithAuthExceptionMessage_ShouldUseProvidedMessage() throws IOException {
        String expectedMessage = "Custom error message";
        String requestUri = "/api/test";
        request.setRequestURI(requestUri);
        when(authException.getMessage()).thenReturn(expectedMessage);

        jwtAuthenticationEntryPoint.commence(request, response, authException);

        verify(objectMapper).writeValue(any(OutputStream.class), any(ExceptionResponse.class));
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    @Test
    void commence_WithNullAuthExceptionMessage_ShouldUseDefaultMessage() throws IOException {
        String requestUri = "/api/test";
        request.setRequestURI(requestUri);
        when(authException.getMessage()).thenReturn(null);

        jwtAuthenticationEntryPoint.commence(request, response, authException);

        verify(objectMapper).writeValue(any(OutputStream.class), any(ExceptionResponse.class));
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    @Test
    void commence_WithOriginalUrlAttribute_ShouldUseOriginalUrl() throws IOException {
        String originalUrl = "/original/url";
        request.setAttribute("originalUrl", originalUrl);
        when(authException.getMessage()).thenReturn("Error message");

        jwtAuthenticationEntryPoint.commence(request, response, authException);

        verify(objectMapper).writeValue(any(OutputStream.class), any(ExceptionResponse.class));
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", response.getContentType());
    }

    @Test
    void commence_ShouldCreateExceptionResponseWithCorrectData() throws IOException {
        String errorMessage = "Test error";
        String requestUri = "/api/test";
        request.setRequestURI(requestUri);
        when(authException.getMessage()).thenReturn(errorMessage);

        jwtAuthenticationEntryPoint.commence(request, response, authException);

        verify(objectMapper).writeValue(any(OutputStream.class), any(ExceptionResponse.class));
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8", response.getContentType());
    }
}
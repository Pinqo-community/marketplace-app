package com.marketplace.auth.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.api.dto.exception.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Entry point for handling authentication errors.
 * Provides formatted JSON response when authentication fails or is missing.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    /**
     * Handles authentication failure scenarios by returning a structured JSON response.
     *
     * @param request HTTP request that triggered the authentication failure
     * @param response HTTP response to be modified
     * @param authException the authentication exception
     * @throws IOException if an input or output error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ExceptionResponse errorResponse = new ExceptionResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                LocalDateTime.now(),
                authException.getMessage() != null ?
                        authException.getMessage() :
                        "Access unauthorized : Token JWT missing or invalid",
                request.getAttribute("originalUrl") != null ?
                        (String) request.getAttribute("originalUrl") :
                        request.getRequestURI(),
                null
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
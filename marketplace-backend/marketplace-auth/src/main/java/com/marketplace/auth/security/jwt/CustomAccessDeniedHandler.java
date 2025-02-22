package com.marketplace.auth.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.api.dto.exception.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Custom handler for access denied exceptions.
 * Provides formatted JSON response when a user attempts to access a resource without proper authorization.
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    /**
     * Handles access denied scenarios by returning a structured JSON response.
     *
     * @param request HTTP request that triggered the access denied
     * @param response HTTP response to be modified
     * @param exception the access denied exception
     * @throws IOException if an input or output error occurs
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
            throws IOException {
        ExceptionResponse errorResponse = new ExceptionResponse(
                HttpServletResponse.SC_FORBIDDEN,
                LocalDateTime.now(),
                "Access denied : You don't have the required permissions for this action",
                request.getAttribute("originalUrl") != null ?
                        (String) request.getAttribute("originalUrl") :
                        request.getRequestURI(),
                null
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}

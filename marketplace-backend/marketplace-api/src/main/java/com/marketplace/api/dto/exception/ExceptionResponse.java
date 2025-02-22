package com.marketplace.api.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponse(
        @Schema(description = "Status code", example="400")
        int status,
        @Schema(description = "Timestamp", example="2024-11-16T14:30:45.123")
        LocalDateTime timestamp,
        @Schema(description = "Error message", example="Invalid Token")
        String message,
        @Schema(description = "Request path", example="/api/v1/auth/register")
        String path,
        @Schema(description = "Validation errors")
        List<ErrorDetail> errors
) {
    public ExceptionResponse {
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("Status code must be between 100 and 599");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }

        timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        message = message.trim();
        path = path.trim();
        errors = errors != null ? List.copyOf(errors) : List.of();
    }

    public ExceptionResponse(
            int status,
            String message,
            String path,
            List<ErrorDetail> errors

    ) {
        this(status, LocalDateTime.now(), message, path, errors);
    }

    public ExceptionResponse(
            int status,
            String message,
            String path
    ) {
        this(status, LocalDateTime.now(), message, path, null);
    }
}

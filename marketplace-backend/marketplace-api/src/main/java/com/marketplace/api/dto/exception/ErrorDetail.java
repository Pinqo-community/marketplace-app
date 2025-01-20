package com.marketplace.api.dto.exception;

public record ErrorDetail(
        String field,
        String message
) {
}

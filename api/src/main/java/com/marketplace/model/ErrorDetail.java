package com.marketplace.model;

public record ErrorDetail(
        String field,
        String message
) {
}

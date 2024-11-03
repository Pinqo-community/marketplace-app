package com.marketplace.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull(message = "Le token de refraichissement est requis")
        String refreshToken
) {
}

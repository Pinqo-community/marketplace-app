package com.marketplace.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull(message = "Le token de refraichissement est requis")
        @NotEmpty(message = "Le refresh token est requis")
        String refreshToken
) {
}

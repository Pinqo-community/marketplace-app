package com.marketplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @Schema(description = "Email", example="test@example.com")
        @NotNull(message = "L'email est requis")
        @NotEmpty(message = "L'email est requis")
        String email,
        @Schema(description = "password", example="Password123!")
        @NotNull(message = "Le mot de passe est requis")
        @NotEmpty(message = "Le mot de passe est requis")
        String password
) {
}

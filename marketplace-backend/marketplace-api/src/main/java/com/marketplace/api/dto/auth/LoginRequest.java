package com.marketplace.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @Schema(description = "Email", example="test@example.com")
        @NotBlank(message = "L'email est requis")
        @NotEmpty(message = "L'email est requis")
        String email,

        @Schema(description = "password", example="Password123!")
        @NotBlank(message = "Le mot de passe est requis")
        @NotEmpty(message = "Le mot de passe est requis")
        String password
) {
}

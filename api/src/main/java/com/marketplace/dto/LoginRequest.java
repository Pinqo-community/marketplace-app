package com.marketplace.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "L'email est requis")
        @NotEmpty(message = "L'email est requis")
        String email,
        @NotNull(message = "Le mot de passe est requis")
        @NotEmpty(message = "Le mot de passe est requis")
        String password
) {
}

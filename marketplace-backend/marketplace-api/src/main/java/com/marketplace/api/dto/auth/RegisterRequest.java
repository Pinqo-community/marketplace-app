package com.marketplace.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Schema(description = "Email", example="test@example.com")
        @NotBlank(message = "L'email est requis")
        @Email(message = "The email is invalid")
        String email,

        @Schema(description = "password", example="Password123!")
        @NotBlank (message = "Le mot de passe est requis")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$%^&*()_+-=])(?=\\S+$).{8,}$",
                message = "Le mot de passe doit contenir au moins 1 majuscule, 1 minuscule, 1 chiffre et 1 caractère spécial"
        )
        @Size(min = 6, message = "Le mot de passe doit contenir minimum 6 caractères")
        String password,

        @Schema(description = "Firstname", example="John")
        @NotBlank (message = "Le prénom est requis")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÿ\\s\\-]+$",
                message = "Le prénom ne doit contenir que des lettres, espaces et tirets"
        )
        String firstname,

        @Schema(description = "Lastname", example="Doe")
        @NotBlank(message = "Le nom est requis")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÿ\\s\\-]+$",
                message = "Le nom ne doit contenir que des lettres, espaces et tirets"
        )
        String lastname
) {
}

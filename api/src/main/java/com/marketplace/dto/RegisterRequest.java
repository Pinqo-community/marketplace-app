package com.marketplace.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotNull (message = "L'email est requis")
        @Email (message = "The email is invalid")
        String email,

        @NotNull (message = "Le mot de passe est requis")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$%^&*()_+-=])(?=\\S+$).{8,}$",
                message = "Le mot de passe doit contenir au moins 1 majuscule, 1 minuscule, 1 chiffre et 1 caractère spécial"
        )
        @Size(min = 6, message = "Le mot de passe doit contenir minimum 6 caractères")
        String password,

        @NotNull (message = "Le prénom est requis")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÿ\\s\\-]+$",
                message = "Le prénom ne doit contenir que des lettres, espaces et tirets"
        )
        String firstname,

        @NotNull (message = "Le nom est requis")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÿ\\s\\-]+$",
                message = "Le nom ne doit contenir que des lettres, espaces et tirets"
        )
        String lastname
) {
}
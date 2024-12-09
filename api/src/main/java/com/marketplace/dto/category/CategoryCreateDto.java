package com.marketplace.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDto (
    @Schema(description = "Nom de la catégorie", example="fruits et légumes")
    @NotBlank(message = "Le nom de la catégorie est obligatoire.")
    String name
) {}
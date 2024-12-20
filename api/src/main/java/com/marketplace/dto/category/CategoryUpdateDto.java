package com.marketplace.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryUpdateDto (
    @Schema(description = "Nom de la catégorie", example="fruits et légumes")
    String name
) {}
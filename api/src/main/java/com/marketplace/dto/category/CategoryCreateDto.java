package com.marketplace.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateDto {

    @Schema(description = "Nom de la catégorie", example="fruits et légumes")
    @NotBlank(message = "Le nom de la catégorie est obligatoire.")
    private String name;
}
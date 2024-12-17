package com.marketplace.dto.Label;


import jakarta.validation.constraints.NotBlank;

public record LabelDto(

        @NotBlank(message = "Le nom du label est obligatoire")
        String name,

        String description
) {}

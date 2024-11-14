package com.marketplace.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Represents a product as a Data Transfer Object (DTO).
 * This DTO is used to receive or send product data via the API.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    /**
     * Product name.
     * Cannot be empty and must be between 1 and 100 characters long.
     */
    @NotBlank(message = "Le nom du produit ne peut pas être vide")
    @Size(min = 1, max = 100, message = "Le nom doit contenir entre 1 et 100 caractères")
    private String name;

    /**
     * Product description.
     * Optional field.
     */
    private String description;

    /**
     * Product photo (URL or path).
     * Optional field.
     */
    private String photo;

    /**
     * Product unit price.
     * Cannot be zero and must be greater than 0.
     */
    @NotNull(message = "Le prix unitaire ne peut pas être nul")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    @Digits(integer = 10, fraction = 2, message = "Le prix doit être un nombre valide avec jusqu'à 10 chiffres et 2 décimales")
    private BigDecimal unitPrice;

    /**
     * Nutritional value of the product.
     * Optional field.
     */
    private String nutritionalValue;

    /**
     * List of product ingredients.
     * Optional field.
     */
    private String listOfIngredients;

    /**
     * Quantity of product in stock.
     * Cannot be zero and must be greater than or equal to 0.
     */
    @NotNull(message = "La quantité en stock ne peut pas être nulle")
    @Min(value = 0, message = "La quantité en stock doit être supérieure ou égale à 0")
    private Integer stockQuantity;

    /**
     * Critical product threshold (minimum quantity before alert).
     * Cannot be zero and must be greater than or equal to 1.
     */
    @NotNull(message = "Le seuil critique ne peut pas être nul")
    @Min(value = 0, message = "Le seuil critique doit être supérieur ou égal à 1")
    private Integer criticalLevel;
}

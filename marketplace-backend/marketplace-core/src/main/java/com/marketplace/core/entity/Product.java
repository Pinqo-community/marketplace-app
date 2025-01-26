package com.marketplace.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represents a product in the marketplace.
 * This entity contains information about a product.
 */

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String photo;
    private BigDecimal unitPrice;
    private String nutritionalValue;
    private String listOfIngredients;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(columnDefinition = "INTEGER DEFAULT 1")
    private Integer maxQuantityByPurchase;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer stepQuantity;

    @Column(columnDefinition = "INTEGER DEFAULT 1")
    private Integer criticalLevel;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default // This ensures a default value when using the Builder pattern
    private Boolean active = true;

}


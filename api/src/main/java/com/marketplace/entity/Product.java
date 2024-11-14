package com.marketplace.entity;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
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
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String photo;
    private BigDecimal unitPrice;
    private String nutritionalValue;
    private String listOfIngredients;
    private Integer stockQuantity;
    private Integer criticalLevel;
}
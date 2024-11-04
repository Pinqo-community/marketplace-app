package com.marketplace.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produits")
@Data // Lombok pour générer les getters et setters
@NoArgsConstructor // Lombok pour le constructeur par défaut
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le nom du produit ne peut pas être nul")
    @Size(min = 1, max = 100, message = "Le nom doit contenir entre 1 et 100 caractères")
    private String nom;

    private String description;

    private String photo;

    @NotNull(message = "Le prix unitaire ne peut pas être nul")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    @Digits(integer = 10, fraction = 2, message = "Le prix doit être un nombre valide avec jusqu'à 10 chiffres et 2 décimales")
    private BigDecimal prixUnitaire;

    private String valeurNutri;

    private String listeIngrédients;

    @NotNull(message = "La quantité en stock ne peut pas être nulle")
    @Min(value = 0, message = "La quantité en stock doit être supérieure ou égale à 0")
    private Integer qteStock;

    @NotNull(message = "Le seuil critique ne peut pas être nul")
    @Min(value = 0, message = "Le seuil critique doit être supérieur ou égal à 0")
    private Integer seuilCritique;

    // Optionnel : Vous pouvez ajouter des méthodes spécifiques si nécessaire
}
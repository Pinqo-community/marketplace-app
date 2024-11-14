package com.marketplace;

import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import com.marketplace.utils.mapper.ProductMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
/*
  Vérifie que ProductManager convertit correctement un ProductDTO en Product avec toutes les valeurs renseignées
 */
public class ProductMapperTest {
    @Autowired
    private ProductMapper productMapper;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testProductDtoToProductMappingWithValidator() {
        ProductDto productDto = ProductDto.builder()
                .name("Test Product")
                .description("Test Description")
                .photo("test-photo-url")
                .unitPrice(new BigDecimal("10.00"))
                .nutritionalValue("High")
                .listOfIngredients("Ingredient1, Ingredient2")
                .stockQuantity(100)
                .criticalLevel(5)
                .build();

        // Validation du ProductDto
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertTrue(violations.isEmpty(), "Le ProductDto contient des violations de contraintes : " + violations);

        Product product = productMapper.toEntity(productDto);
        assertNotNull(product);
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals("test-photo-url", product.getPhoto());
        assertEquals(new BigDecimal("10.00"), product.getUnitPrice());
        assertEquals("High", product.getNutritionalValue());
        assertEquals("Ingredient1, Ingredient2", product.getListOfIngredients());
        assertEquals(100, product.getStockQuantity());
        assertEquals(5, product.getCriticalLevel());

    }

    /**
     * simule un ProductDto invalide
     * puis vérifie que les contraintes de validation sont bien déclenchées et signalées par le Validator.
     */
    @Test
    public void testProductDtoToProductMapping_withInvalidDto() {
        // Création d'un ProductDto avec un champ obligatoire manquant
        ProductDto invalidProductDto = ProductDto.builder()
                .description("Test Description")
                .photo("test-photo-url")
                .unitPrice(null) // Champ obligatoire manquant
                .stockQuantity(100)
                .criticalLevel(5)
                .build();

        // Validation de ProductDto et vérification de la violation
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(invalidProductDto);
        assertFalse(violations.isEmpty(), "Le ProductDto devrait contenir des violations de contraintes");
    }
}

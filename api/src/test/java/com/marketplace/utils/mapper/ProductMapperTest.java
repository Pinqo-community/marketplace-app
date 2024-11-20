//package com.marketplace.utils.mapper;
//
//import com.marketplace.dto.ProductDto;
//import com.marketplace.entity.Product;
//import jakarta.validation.ConstraintViolation;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigDecimal;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//
//public class ProductMapperTest {
//
//    @Mock
//    private ProductMapper productMapper;
//
//    @InjectMocks
//    private jakarta.validation.Validator validator;
//
//    @InjectMocks
//    private ProductMapperTest testInstance;
//
//
//    @Test
//    public void testProductDtoToProductMappingWithValidator() {
//        ProductDto productDto = ProductDto.builder()
//                .name("Test Product")
//                .description("Test Description")
//                .photo("test-photo-url")
//                .unitPrice(new BigDecimal("10.00"))
//                .nutritionalValue("High")
//                .listOfIngredients("Ingredient1, Ingredient2")
//                .stockQuantity(100)
//                .criticalLevel(5)
//                .build();
//
//
//        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
//        assertTrue(violations.isEmpty(), "Le ProductDto contient des violations de contraintes : " + violations);
//
//        Product product = productMapper.toEntity(productDto);
//        assertNotNull(product);
//        assertEquals("Test Product", product.getName());
//        assertEquals("Test Description", product.getDescription());
//        assertEquals("test-photo-url", product.getPhoto());
//        assertEquals(new BigDecimal("10.00"), product.getUnitPrice());
//        assertEquals("High", product.getNutritionalValue());
//        assertEquals("Ingredient1, Ingredient2", product.getListOfIngredients());
//        assertEquals(100, product.getStockQuantity());
//        assertEquals(5, product.getCriticalLevel());
//
//    }
//
//    /**
//     * simulates an invalid ProductDto
//     * then checks that validation constraints are triggered and signaled by the Validator.
//     */
//    @Test
//    public void testProductDtoToProductMappingWithInvalidDto() {
//        ProductDto invalidProductDto = ProductDto.builder()
//                .description("Test Description")
//                .photo("test-photo-url")
//                .unitPrice(null) // Champ obligatoire manquant
//                .stockQuantity(100)
//                .criticalLevel(5)
//                .build();
//
//        Set<ConstraintViolation<ProductDto>> violations = validator.validate(invalidProductDto);
//        assertFalse(violations.isEmpty(), "Le ProductDto devrait contenir des violations de contraintes");
//    }
//}



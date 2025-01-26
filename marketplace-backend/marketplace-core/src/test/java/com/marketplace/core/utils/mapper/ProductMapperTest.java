package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.product.ProductDto;
import com.marketplace.core.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ProductMapperTest {
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
    }

    @Test
    void toDto_whenValidProduct_thenMapAllFields() {
        // Given
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .photo("test-photo.jpg")
                .unitPrice(new BigDecimal("10.99"))
                .nutritionalValue("High protein")
                .listOfIngredients("Ingredient 1, Ingredient 2")
                .stockQuantity(100)
                .maxQuantityByPurchase(10)
                .stepQuantity(1)
                .criticalLevel(5)
                .active(true)
                .build();

        // When
        ProductDto result = productMapper.toDto(product);

        // Then
        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getName()).isEqualTo(product.getName());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getPhoto()).isEqualTo(product.getPhoto());
        assertThat(result.getUnitPrice()).isEqualTo(product.getUnitPrice());
        assertThat(result.getNutritionalValue()).isEqualTo(product.getNutritionalValue());
        assertThat(result.getListOfIngredients()).isEqualTo(product.getListOfIngredients());
        assertThat(result.getStockQuantity()).isEqualTo(product.getStockQuantity());
        assertThat(result.getMaxQuantityByPurchase()).isEqualTo(product.getMaxQuantityByPurchase());
        assertThat(result.getStepQuantity()).isEqualTo(product.getStepQuantity());
        assertThat(result.getCriticalLevel()).isEqualTo(product.getCriticalLevel());
        assertThat(result.getActive()).isEqualTo(product.getActive());
    }

    @Test
    void toDto_whenNullProduct_thenReturnNull() {
        assertThat(productMapper.toDto(null)).isNull();
    }

    @Test
    void toEntity_whenValidDto_thenMapAllFields() {
        // Given
        ProductDto dto = new ProductDto();
        dto.setId(1L);
        dto.setName("Test Product");
        dto.setDescription("Test Description");
        dto.setPhoto("test-photo.jpg");
        dto.setUnitPrice(new BigDecimal("10.99"));
        dto.setNutritionalValue("High protein");
        dto.setListOfIngredients("Ingredient 1, Ingredient 2");
        dto.setStockQuantity(100);
        dto.setMaxQuantityByPurchase(10);
        dto.setStepQuantity(1);
        dto.setCriticalLevel(5);
        dto.setActive(true);

        // When
        Product result = productMapper.toEntity(dto);

        // Then
        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getName()).isEqualTo(dto.getName());
        assertThat(result.getDescription()).isEqualTo(dto.getDescription());
        assertThat(result.getPhoto()).isEqualTo(dto.getPhoto());
        assertThat(result.getUnitPrice()).isEqualTo(dto.getUnitPrice());
        assertThat(result.getNutritionalValue()).isEqualTo(dto.getNutritionalValue());
        assertThat(result.getListOfIngredients()).isEqualTo(dto.getListOfIngredients());
        assertThat(result.getStockQuantity()).isEqualTo(dto.getStockQuantity());
        assertThat(result.getMaxQuantityByPurchase()).isEqualTo(dto.getMaxQuantityByPurchase());
        assertThat(result.getStepQuantity()).isEqualTo(dto.getStepQuantity());
        assertThat(result.getCriticalLevel()).isEqualTo(dto.getCriticalLevel());
        assertThat(result.getActive()).isEqualTo(dto.getActive());
    }

    @Test
    void toEntity_whenNullDto_thenReturnNull() {
        assertThat(productMapper.toEntity(null)).isNull();
    }

    @Test
    void toDtoList_whenValidProducts_thenMapAllProducts() {
        // Given
        Product product1 = Product.builder()
                .id(1L)
                .name("Product 1")
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Product 2")
                .build();

        List<Product> products = Arrays.asList(product1, product2);

        // When
        List<ProductDto> result = productMapper.toDtoList(products);

        // Then
        assertThat(result)
                .hasSize(2)
                .extracting(ProductDto::getId, ProductDto::getName)
                .containsExactly(
                        tuple(1L, "Product 1"),
                        tuple(2L, "Product 2")
                );
    }

    @Test
    void toDtoList_whenNullList_thenReturnNull() {
        assertThat(productMapper.toDtoList(null)).isNull();
    }

    @Test
    void toDtoList_whenEmptyList_thenReturnEmptyList() {
        assertThat(productMapper.toDtoList(List.of())).isEmpty();
    }
}
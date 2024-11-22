package com.marketplace.controller;


import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import com.marketplace.service.ProductService;
import com.marketplace.utils.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController productController;

    private ProductDto productDto;
    private Product product;

    @BeforeEach
    public void setup() {
        // Setup Product and ProductDto
        product = new Product(1L, "Miel du Pays", "Pot de 300g", "img_url", new BigDecimal("10.00"), "High", "", 10, 2, true);
        productDto = new ProductDto(1L,"Miel du Pays", "Pot de 300g", "img_url", new BigDecimal("10.00"), "High", "", 10, 2, true);
    }

    /**
     * Given - ProductDto is set
     *  When - Controller method is invoked
     *  Then - Response status should be CREATED (201) and the content should match
     */
    @Test
    public void givenProductDto_whenCreateProduct_thenReturnCreatedProduct() {
        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);
        ResponseEntity<ProductDto> response = productController.createProduct(productDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Miel du Pays", response.getBody().getName());
        assertEquals("Pot de 300g", response.getBody().getDescription());
        assertEquals("img_url", response.getBody().getPhoto());
        assertEquals("10.00", response.getBody().getUnitPrice().toString());
        assertEquals("High", response.getBody().getNutritionalValue());
        assertEquals("", response.getBody().getListOfIngredients());
        assertEquals("10", response.getBody().getStockQuantity().toString());
        assertEquals("2", response.getBody().getCriticalLevel().toString());
        assertEquals(true, response.getBody().getActive());

        // Verify interaction with service
        verify(productService, times(1)).createProduct(any(ProductDto.class));
   }

    /**
     *  Given - Product exists with the given ID
     *  When - Controller method is invoked
     *  Then - Response status should be OK (200) and the content should match
     */
    @Test
    public void givenValidId_whenGetProductById_thenReturnProductDto() {
        //
        when(productService.getProductById(1L)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);
        ResponseEntity<ProductDto> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Miel du Pays", response.getBody().getName());
        assertEquals("Pot de 300g", response.getBody().getDescription());
        assertEquals("img_url", response.getBody().getPhoto());
        assertEquals("10.00", response.getBody().getUnitPrice().toString());
        assertEquals("High", response.getBody().getNutritionalValue());
        assertEquals("", response.getBody().getListOfIngredients());
        assertEquals("10", response.getBody().getStockQuantity().toString());
        assertEquals("2", response.getBody().getCriticalLevel().toString());
        assertEquals(true, response.getBody().getActive());


        // Verify interaction with service
        verify(productService, times(1)).getProductById(1L);
    }

    /**
     * Given - Product exists
     * When - Controller method is invoked
     * Then - Response status should be NO_CONTENT (204)
     */
    @Test
    public void givenProductId_whenDeleteProduct_thenReturnNoContent() {
        doNothing().when(productService).deleteProduct(1L);
        ResponseEntity<HttpStatus> response = productController.deleteProduct(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify interaction with service
        verify(productService, times(1)).deleteProduct(1L);
    }

    /**
     * Given - ProductDto and ID
     * When - Controller method is invoked
     * Then - Response status should be OK (200) and the content should match
     */
    @Test
    public void givenProductDto_whenUpdateProduct_thenReturnUpdatedProductDto() {
        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ResponseEntity<ProductDto> response = productController.updateProduct(1L, productDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Miel du Pays", response.getBody().getName());
        assertEquals("Pot de 300g", response.getBody().getDescription());
        assertEquals("img_url", response.getBody().getPhoto());
        assertEquals("10.00", response.getBody().getUnitPrice().toString());
        assertEquals("High", response.getBody().getNutritionalValue());
        assertEquals("", response.getBody().getListOfIngredients());
        assertEquals("2", response.getBody().getStockQuantity().toString());
        assertEquals("2", response.getBody().getCriticalLevel().toString());
        assertEquals(true, response.getBody().getActive());


        // Verify interaction with service
        verify(productService, times(1)).updateProduct(eq(1L), any(ProductDto.class));
    }

    /**
     * Given - Product is not found with the given ID
     * When - Controller method is invoked
     * Then - Response status should be NOT_FOUND (404)
     */
    @Test
    public void givenInvalidProductId_whenGetProductById_thenReturnNotFound() {

        when(productService.getProductById(999L)).thenReturn(null);

        ResponseEntity<ProductDto> response = productController.getProductById(999L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify interaction with service
        verify(productService, times(1)).getProductById(999L);
    }
}

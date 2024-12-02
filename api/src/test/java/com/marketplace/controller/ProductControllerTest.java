package com.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.annotation.ControllerWebMvcTest;
import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import com.marketplace.exception.GlobalExceptionHandler;
import com.marketplace.exception.NotFoundException;
import com.marketplace.service.ProductService;
import com.marketplace.utils.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerWebMvcTest (ProductController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("Tests for ProductController")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;
    private ProductDto baseProductDto;

    @BeforeEach
    public void setup() {
         baseProductDto = new ProductDto(
                1L,
                "Miel de lavande",
                "Pot de 300g",
                "img_url",
                new BigDecimal("10.00"),
                "High",
                "",
                30,
                1,
                50,
                1,
                2,
                true
        );
    }


    @Nested
    @DisplayName("POST /products - Create Product")
    class CreateProduct {
        @Test
        @DisplayName("Should create a product successfully")
        void shouldCreateProduct() throws Exception {
            //Arrange
            Mockito.when(productService.createProduct(any(ProductDto.class))).thenReturn(baseProductDto);

            // Act & Assert
            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(baseProductDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Miel de lavande"));

            verify(productService, times(1)).createProduct(any(ProductDto.class));
        }

        @Test
        @DisplayName("Should return 400 when input is invalid")
        void shouldReturnBadRequestForInvalidInput() throws Exception {
            //Arrange
            ProductDto invalidProductDto = new ProductDto(null, null, "desc", null, null, "Low", "", 0, 0, 0, 0, 0, false);

            // Act & Assert
            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidProductDto)))
                    .andExpect(status().isBadRequest());

            verify(productService, times(0)).createProduct(any(ProductDto.class));
        }
    }

    @Nested
    @DisplayName("GET /products/available - Get Available Products")
    class GetAvailableProducts {
        @Test
        @DisplayName("Should return available products")
        void shouldReturnAvailableProducts() throws Exception {
            //Arrange
            Product product = new Product();
            List<Product> products = List.of(product);

            Mockito.when(productService.getAvailableProducts()).thenReturn(products);
            Mockito.when(productMapper.toDtoList(products)).thenReturn(List.of(baseProductDto));

            // Act & Assert
            mockMvc.perform(get("/products/available"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(baseProductDto.getId()))
                    .andExpect(jsonPath("$[0].name").value(baseProductDto.getName()));

            verify(productService, times(1)).getAvailableProducts();
        }

        @Test
        @DisplayName("Should return 204 when no products are available")
        void shouldReturnNoContent() throws Exception {
            //Arrange
            Mockito.when(productService.getAvailableProducts()).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(get("/products/available"))
                    .andExpect(status().isNoContent());

            verify(productService, times(1)).getAvailableProducts();
        }
    }

    @Nested
    @DisplayName("GET /products/{id} - Get Product By ID")
    class GetProductById {
        @Test
        @DisplayName("Should return product by ID")
        void shouldReturnProductById() throws Exception {
            //Arrange
            Mockito.when(productService.getProductById(anyLong())).thenReturn(new Product());
            Mockito.when(productMapper.toDto(any(Product.class))).thenReturn(baseProductDto);

            // Act & Assert
            mockMvc.perform(get("/products/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(baseProductDto.getId()))
                    .andExpect(jsonPath("$.name").value(baseProductDto.getName()));

            verify(productService, times(1)).getProductById(anyLong());
        }

        @Test
        @DisplayName("Should return 404 when product not found")
        void shouldReturnNotFound() throws Exception {
            //Arrange
            Mockito.when(productService.getProductById(anyLong())).thenThrow(new NotFoundException("Product not found"));

            //Act & Assert
            mockMvc.perform(get("/products/{id}", 1L))
                    .andExpect(status().isNotFound());

            verify(productService, times(1)).getProductById(anyLong());
        }
    }

    @Nested
    @DisplayName("PUT /products/{id} - Update Product")
    class UpdateProduct {
        @Test
        @DisplayName("Should update product successfully")
        void shouldUpdateProduct() throws Exception {
            //Arrange
            Product updatedProduct = new Product();
            ProductDto updatedDto = new ProductDto(1L, "Updated Miel", "Pot 500g", "new_img", new BigDecimal("15.00"), "High", "", 50, 1, 100, 1, 2, true);
            Mockito.when(productService.updateProduct(anyLong(), any(ProductDto.class))).thenReturn(updatedProduct);
            Mockito.when(productMapper.toDto(updatedProduct)).thenReturn(updatedDto);

        // Act & Assert
            mockMvc.perform(put("/products/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated Miel"));

            verify(productService, times(1)).updateProduct(anyLong(), any(ProductDto.class));
        }
    }

    @Nested
    @DisplayName("DELETE /products/{id} - Delete Product")
    class DeleteProduct {
        @Test
        @DisplayName("Should delete product successfully")
        void shouldDeleteProduct() throws Exception {
            //Arrange
            Mockito.doNothing().when(productService).deleteProduct(anyLong());

            // Act & Assert
            mockMvc.perform(delete("/products/{id}", 1L))
                    .andExpect(status().isNoContent());

            verify(productService, times(1)).deleteProduct(anyLong());
        }

        @Test
        @DisplayName("Should return 404 when product not found")
        void shouldReturnNotFoundForDelete() throws Exception {
            //Arrange
            Mockito.doThrow(new NotFoundException("Product not found")).when(productService).deleteProduct(anyLong());
            // Act & Assert
            mockMvc.perform(delete("/products/{id}", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Product not found"));

            verify(productService, times(1)).deleteProduct(anyLong());
        }
    }
}

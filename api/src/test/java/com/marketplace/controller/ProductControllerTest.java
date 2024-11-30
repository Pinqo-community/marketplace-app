package com.marketplace.controller;

import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import com.marketplace.exception.NotFoundException;
import com.marketplace.exception.UnavailableProductException;
import com.marketplace.service.ProductService;
import com.marketplace.utils.mapper.ProductMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("ProductController")
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController productController;

    @Nested
    @DisplayName("Tests for createProduct method")
    class CreateProductTests {

        @Test
        @DisplayName("Should create a new product and return it with HTTP status 201")
        void createProduct_ValidRequest_ReturnsCreatedProduct() {
            // Arrange
            ProductDto productDto = ProductDto.builder().name("Test Product").build();
            ProductDto savedProductDto = ProductDto.builder().id(1L).name("Test Product").build();

            when(productService.createProduct(productDto)).thenReturn(savedProductDto);

            // Act
            ResponseEntity<ProductDto> response = productController.createProduct(productDto);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(savedProductDto, response.getBody());
            verify(productService, times(1)).createProduct(productDto);
        }
    }

    @Nested
    @DisplayName("Tests for getAvailableProducts method")
    class GetAvailableProductsTests {

        @Test
        @DisplayName("Should return available products with HTTP status 200")
        void getAvailableProducts_HasProducts_ReturnsProducts() {
            // Arrange
            Product product = Product.builder().id(1L).name("Product 1").build();
            ProductDto productDto = ProductDto.builder().id(1L).name("Product 1").build();

            when(productService.getAvailableProducts()).thenReturn(List.of(product));
            when(productMapper.toDtoList(List.of(product))).thenReturn(List.of(productDto));

            // Act
            ResponseEntity<List<ProductDto>> response = productController.getAvailableProducts();

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals(productDto, response.getBody().get(0));
            verify(productService, times(1)).getAvailableProducts();
            verify(productMapper, times(1)).toDtoList(List.of(product));
        }

        @Test
        @DisplayName("Should return HTTP status 204 when no products are available")
        void getAvailableProducts_NoProducts_ReturnsNoContent() {
            // Arrange
            when(productService.getAvailableProducts()).thenThrow(new NotFoundException("Aucun produit disponible avec un stock supérieur à zéro."));

            // Act
            ResponseEntity<List<ProductDto>> response = productController.getAvailableProducts();

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            assertNull(response.getBody());
            verify(productService, times(1)).getAvailableProducts();
        }
    }

    @Nested
    @DisplayName("Tests for getProductById method")
    class GetProductByIdTests {

        @Test
        @DisplayName("Should return product with HTTP status 200")
        void getProductById_HasProduct_ReturnsProduct() {
            // Arrange
            Product product = Product.builder().id(1L).name("Product 1").build();
            ProductDto productDto = ProductDto.builder().id(1L).name("Product 1").build();

            when(productService.getProductById(1L)).thenReturn(product);
            when(productMapper.toDto(product)).thenReturn(productDto);

            // Act
            ResponseEntity<ProductDto> response = productController.getProductById(1L);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(productDto, response.getBody());
            verify(productService, times(1)).getProductById(1L);
            verify(productMapper, times(1)).toDto(product);
        }

        @Test
        @DisplayName("Should return HTTP status 404 when product is not found")
        void getProductById_ProductNotFound_ReturnsNotFound() {
            // Arrange
            when(productService.getProductById(1L)).thenThrow(new NotFoundException("Ce produit n'existe pas"));

            // Act
            ResponseEntity<ProductDto> response = productController.getProductById(1L);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            verify(productService, times(1)).getProductById(1L);
        }
    }

    @Test
    @DisplayName("Should return HTTP status 410 when product is inactive or unavailable")
    void getProductById_ProductInactive_ReturnsGone() {
        // Arrange
        when(productService.getProductById(1L)).thenThrow(new UnavailableProductException("Le produit avec l'ID 1 est inactif ou indisponible."));

        // Act
        ResponseEntity<ProductDto> response = productController.getProductById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.GONE, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService, times(1)).getProductById(1L);
    }


    @Nested
    @DisplayName("Tests for getProductsByStatus method")
    class GetProductsByStatusTests {

        @Test
        @DisplayName("Should return products with HTTP status 200")
        void getProductsByStatus_HasProducts_ReturnsProducts() {
            // Arrange
            ProductDto productDto = ProductDto.builder().id(1L).name("Product 1").build();

            when(productService.getProductsByStatus(true)).thenReturn(List.of(productDto));

            // Act
            ResponseEntity<List<ProductDto>> response = productController.getProductsByStatus(true);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals(productDto, response.getBody().get(0));
            verify(productService, times(1)).getProductsByStatus(true);
        }

        @Test
        @DisplayName("Should return HTTP status 400 when active is null")
        void getProductsByStatus_NullActive_ThrowsBadRequest() {
            // Arrange
            doThrow(new IllegalArgumentException("Le paramètre 'active' ne peut pas être null."))
                    .when(productService).getProductsByStatus(null);

            // Act
            ResponseEntity<List<ProductDto>> response = null;
            try {
                response = productController.getProductsByStatus(null);
            } catch (IllegalArgumentException ex) {
                assertEquals("Le paramètre 'active' ne peut pas être null.", ex.getMessage());
            }

            // Assert
            assertNull(response);
            verify(productService, times(1)).getProductsByStatus(null);
        }

        @Test
        @DisplayName("Should return HTTP status 204 when no products are available")
        void getProductsByStatus_NoProducts_ReturnsNoContent() {
            // Arrange
            when(productService.getProductsByStatus(false)).thenThrow(new NotFoundException("Aucun produit trouvé avec l'état actif = false"));

            // Act
            ResponseEntity<List<ProductDto>> response = productController.getProductsByStatus(false);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            assertNull(response.getBody());
            verify(productService, times(1)).getProductsByStatus(false);
        }
    }

    @Nested
    @DisplayName("Tests for updateProduct method")
    class UpdateProductTests {

        @Test
        @DisplayName("Should update and return product with HTTP status 200")
        void updateProduct_ValidRequest_ReturnsUpdatedProduct() {
            // Arrange
            ProductDto productDto = ProductDto.builder().id(1L).name("Updated Product").build();
            Product product = Product.builder().id(1L).name("Updated Product").build();

            when(productService.updateProduct(1L, productDto)).thenReturn(product);
            when(productMapper.toDto(product)).thenReturn(productDto);

            // Act
            ResponseEntity<ProductDto> response = productController.updateProduct(1L, productDto);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(productDto, response.getBody());
            verify(productService, times(1)).updateProduct(1L, productDto);
            verify(productMapper, times(1)).toDto(product);
        }

        @Test
        @DisplayName("Should return HTTP status 404 when product is not found")
        void updateProduct_ProductNotFound_ReturnsNotFound() {
            // Arrange
            ProductDto productDto = ProductDto.builder().id(1L).name("Updated Product").build();

            when(productService.updateProduct(1L, productDto)).thenThrow(new NotFoundException("Ce produit n'existe pas"));

            // Act
            ResponseEntity<ProductDto> response = productController.updateProduct(1L, productDto);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            verify(productService, times(1)).updateProduct(1L, productDto);
        }
    }

    @Nested
    @DisplayName("Tests for deleteProduct method")
    class DeleteProductTests {

        @Test
        @DisplayName("Should delete product with HTTP status 204")
        void deleteProduct_HasProduct_ReturnsNoContent() {
            // Arrange
            doNothing().when(productService).deleteProduct(1L);

            // Act
            ResponseEntity<HttpStatus> response = productController.deleteProduct(1L);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(productService, times(1)).deleteProduct(1L);
        }

        @Test
        @DisplayName("Should return HTTP status 404 when product is not found")
        void deleteProduct_ProductNotFound_ReturnsNotFound() {
            // Arrange
            doThrow(new NotFoundException("Ce produit n'existe pas")).when(productService).deleteProduct(1L);

            // Act
            ResponseEntity<HttpStatus> response = productController.deleteProduct(1L);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            verify(productService, times(1)).deleteProduct(1L);
        }
    }

}


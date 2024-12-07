package com.marketplace.Service.impl;

import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import com.marketplace.exception.IllegalArgumentException;
import com.marketplace.exception.NotFoundException;
import com.marketplace.repository.ProductRepository;
import com.marketplace.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("ProductService")
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    // Nested for createProduct
    @Nested
    @DisplayName("createProduct")
    @Tag("Tests for createProduct method")
    class CreateProductTest {

        @Test
        @DisplayName("Should create and save a valid product" )
        void createProduct_ValidProduct_SavesProduct() {
            // Arrange
            ProductDto productDto = new ProductDto();
            productDto.setMinQuantity(1);
            productDto.setMaxQuantity(5);
            Product savedProduct = Product.builder()
                    .id(1L)
                    .minQuantity(1)
                    .maxQuantity(5)
                    .build();

            when(productRepository.save(argThat(new ProductMatcher(1, 5)))).thenReturn(savedProduct);

            // Act
            ProductDto result = productService.createProduct(productDto);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(productRepository, times(1)).save(argThat(new ProductMatcher(1, 5)));
        }

        /**
         * Custom ArgumentMatcher for Product.
         */
        static class ProductMatcher implements ArgumentMatcher<Product> {
            private final Integer expectedMinQuantity;
            private final Integer expectedMaxQuantity;

            public ProductMatcher(Integer expectedMinQuantity, Integer expectedMaxQuantity) {
                this.expectedMinQuantity = expectedMinQuantity;
                this.expectedMaxQuantity = expectedMaxQuantity;
            }

            @Override
            public boolean matches(Product product) {
                return product != null &&
                        product.getMinQuantity() != null &&
                        product.getMaxQuantity() != null &&
                        product.getMinQuantity().equals(expectedMinQuantity) &&
                        product.getMaxQuantity().equals(expectedMaxQuantity);
            }

            @Override
            public String toString() {
                return String.format("Product with minQuantity=%d and maxQuantity=%d", expectedMinQuantity, expectedMaxQuantity);
            }
        }

        @Test
        @DisplayName("Should throwException when the minimum quantity is greater than the maximum quantity")
        void createProduct_MinGreaterThanMax_ThrowsException() {
            // Arrange
            ProductDto productDto = new ProductDto();
            productDto.setMinQuantity(5);
            productDto.setMaxQuantity(1);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> productService.createProduct(productDto));
            assertEquals("La quantité minimale doit être inférieure à la quantité maximale", exception.getMessage());
        }
    }

    // Nested tests for getAvailableProducts
    @Nested
    @Tag("GetAvailableProducts")
    @DisplayName("Tests for getAvailableProducts method")
    class GetAvailableProductsTest {

        @Test
        @DisplayName("Should return available products when stock > 0 and active = true")
        void getAvailableProducts_ReturnsProducts() {
            // Arrange
            Product product = Product.builder().id(1L).active(true).stockQuantity(10).build();
            when(productRepository.findByActiveAndStockQuantityGreaterThan(true, 0))
                    .thenReturn(List.of(product));

            // Act
            List<Product> result = productService.getAvailableProducts();

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).getId());
            verify(productRepository, times(1))
                    .findByActiveAndStockQuantityGreaterThan(true, 0);
        }

        @Test
        @DisplayName("Should return a empty list when no products are available")
        void getAvailableProducts_NoProducts_ReturnsNoContent() {
            // Arrange
            when(productRepository.findByActiveAndStockQuantityGreaterThan(true, 0))
                    .thenReturn(Collections.emptyList());

            // Act
            List<Product> result = productService.getAvailableProducts();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(productRepository, times(1))
                    .findByActiveAndStockQuantityGreaterThan(true, 0);
        }
    }

    // Nested tests for getProductById
    @Nested
    @Tag("GetProductById")
    @DisplayName("Tests for getProductById method")
    class GetProductByIdTest {

        @Test
        @DisplayName("Should return product when product is active")
        void getProductById_ReturnsProduct() {
            // Arrange
            Product product = Product.builder().id(1L).active(true).build();
            when(productRepository.findByIdAndActive(1L)).thenReturn(Optional.of(product));

            // Act
            Product result = productService.getProductById(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(productRepository, times(1)).findByIdAndActive(1L);
        }

        @Test
        @DisplayName("Should throw NotFoundException when product is inactive or does not exist")
        void getProductById_InactiveProductOrNotFound_ThrowsNotFoundException() {
            // Arrange
            when(productRepository.findByIdAndActive(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> productService.getProductById(1L));
            verify(productRepository, times(1)).findByIdAndActive(1L);
        }
    }

    // Nested tests for getProductsByStatus
    @Nested
    @Tag("GetProductsByStatus")
    @DisplayName("Tests for getProductsByStatus method")
    class GetProductsByStatusTest {

        @Test
        @DisplayName("Should return products when active = true")
        void getProductsByStatus_ReturnsProducts() {
            // Arrange
            Product product = Product.builder().id(1L).active(true).build();
            when(productRepository.findByActive(true)).thenReturn(List.of(product));

            // Act
            List<ProductDto> result = productService.getProductsByStatus(true);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(productRepository, times(1)).findByActive(true);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when active is null")
        void getProductsByStatus_NullActive_ThrowsIllegalArgumentException() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> productService.getProductsByStatus(null));
        }

        @Test
        @DisplayName("Should return product list when products are found")
        void getProductsByStatus_ProductsFound_ReturnsProductList() {
            // Arrange
            Product product = new Product(1L, "Test Product",null,null,null,null,null,1,1,10,1,1,true);
            List<Product> productList = List.of(product);

            when(productRepository.findByActive(true)).thenReturn(productList);

            // Act
            List<ProductDto> result = productService.getProductsByStatus(true);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Product", result.get(0).getName());
            verify(productRepository, times(1)).findByActive(true);
        }


        @Test
        @DisplayName("Should throw NotFoundException when no products found")
        void getProductsByStatus_NoProducts_ReturnsEmptyList() {
            // Arrange
            when(productRepository.findByActive(false)).thenReturn(Collections.emptyList());

            // Act
            List<ProductDto> result = productService.getProductsByStatus(false);

            //  Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(productRepository, times(1)).findByActive(false);
        }
    }

    // Nested tests for updateProduct
    @Nested
    @Tag("UpdateProduct")
    @DisplayName("Tests for updateProduct method")
    class UpdateProductTest {

        @Test
        @DisplayName("Should update and return product")
        void updateProduct_ReturnsUpdatedProduct() {
            // Arrange
            Product existingProduct = Product.builder().id(1L).build();
            Product updatedProduct = Product.builder().id(1L).name("Updated Name").build();
            ProductDto productDto = ProductDto.builder().name("Updated Name").build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
            when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

            // Act
            Product result = productService.updateProduct(1L, productDto);

            // Assert
            assertNotNull(result);
            assertEquals("Updated Name", result.getName());
            verify(productRepository, times(1)).findById(1L);
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw NotFoundException when product not found")
        void updateProduct_ProductNotFound_ThrowsNotFoundException() {
            // Arrange
            when(productRepository.findById(1L)).thenReturn(Optional.empty());
            ProductDto productDto = ProductDto.builder().name("Updated Name").build();

            // Act & Assert
            assertThrows(NotFoundException.class, () -> productService.updateProduct(1L, productDto));
        }
    }

    // Nested tests for deleteProduct
    @Nested
    @Tag("DeleteProduct")
    @DisplayName("Tests for deleteProduct method")
    class DeleteProductTest {

        @Test
        @DisplayName("Should delete product when exists")
        void deleteProduct_DeletesProduct() {
            // Arrange
            Long productId = 1L;
            when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));
            doNothing().when(productRepository).deleteById(productId);

            // Act
            productService.deleteProduct(productId);

            // Assert
            verify(productRepository, times(1)).findById(productId);
            verify(productRepository, times(1)).deleteById(productId);
        }

        @Test
        @DisplayName("Should throw NotFoundException when product does not exist")
        void deleteProduct_ProductNotFound_ThrowsNotFoundException() {
            // Arrange
            Long productId = 1L;
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            // Act & Assert
            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                productService.deleteProduct(productId);
            });

            assertEquals("Le produit avec l'ID " + productId + " n'existe pas.", exception.getMessage());
            verify(productRepository, never()).deleteById(productId);
            verify(productRepository, times(1)).findById(productId);
        }
    }
}




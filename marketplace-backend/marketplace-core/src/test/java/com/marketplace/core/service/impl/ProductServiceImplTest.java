package com.marketplace.core.service.impl;

import com.marketplace.api.dto.product.ProductDto;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.core.entity.Product;
import com.marketplace.core.repository.ProductRepository;
import com.marketplace.core.utils.mapper.ProductMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    /**
     * This nested test class verifies the functionality of the createProduct method
     * in the ProductService implementation. It contains unit tests for both valid and
     * invalid scenarios when creating products.
     */
    @Nested
    class CreateProduct {
        @Test
        void whenValidProduct_thenReturnDto() {
            ProductDto inputDto = new ProductDto();
            inputDto.setStockQuantity(100);
            inputDto.setMaxQuantityByPurchase(50);

            Product product = new Product();
            Product savedProduct = new Product();
            ProductDto outputDto = new ProductDto();

            when(productMapper.toEntity(inputDto)).thenReturn(product);
            when(productRepository.save(product)).thenReturn(savedProduct);
            when(productMapper.toDto(savedProduct)).thenReturn(outputDto);

            ProductDto result = productService.createProduct(inputDto);

            assertThat(result).isEqualTo(outputDto);
            verify(productMapper).toEntity(inputDto);
            verify(productRepository).save(product);
            verify(productMapper).toDto(savedProduct);
        }

        @Test
        void whenInvalidQuantity_thenThrowException() {
            ProductDto inputDto = new ProductDto();
            inputDto.setStockQuantity(10);
            inputDto.setMaxQuantityByPurchase(20);

            assertThatThrownBy(() -> productService.createProduct(inputDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("La quantité maximale par achat doit être supérieure à 0 et inférieure ou égale à la quantité en stock");
        }
    }

    /**
     * Unit test for the `getAvailableProducts` method in `ProductService`.
     * Verifies that only active products with stock > 0 are returned in a paginated format.
     *
     * Key scenarios tested:
     * - Correct number of products returned based on page size and number.
     * - Accuracy of total elements and pages in the paginated result.
     * - Proper interaction with the mocked `productRepository`.
     *
     * Approach:
     * - Uses `PageImpl` to mock paginated results.
     * - Assertions validate content size, total elements, and pages.
     * - Repository interactions checked with `when` and `verify`.
     */
    @Nested
    class GetAvailableProducts {
        @Test
        void shouldReturnPaginatedAvailableProducts() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10); // first page, 10 elements
            List<Product> expectedProducts = List.of(
                    new Product(1L, "Miel de lavande", "Pot de 300g", "img_url",
                            new BigDecimal("10.00"), "High", "", 30, 50, 1, 2, true),
                    new Product(2L, "Ratatouille", "Pot de 500g", "img2_url",
                            new BigDecimal("8.00"), "High", "", 20, 10, 1, 2, true)
            );

            Page<Product> pagedProducts = new PageImpl<>(expectedProducts, pageable, expectedProducts.size());

            when(productRepository.findByActiveAndStockQuantityGreaterThan(true, 0, pageable)).thenReturn(pagedProducts);

            // Act
            Page<ProductDto> result = productService.getAvailableProducts(pageable);

            // Assert
            // Assert
            assertEquals(2, result.getContent().size(), "The number of products in the content should match.");
            assertEquals(2, result.getTotalElements(), "The number of total elements should match.");
            assertEquals(1, result.getTotalPages(), "The number of total pages should match.");
            assertEquals(10, result.getSize(), "The size of the page should match.");

            verify(productRepository).findByActiveAndStockQuantityGreaterThan(true, 0, pageable);
        }
    }

    /**
     * Test class for verifying the functionality of the `getProductsByStatus` method
     * of the ProductServiceImpl. This class validates the retrieval of paginated
     * ProductDto objects filtered by their active status.
     */
    @Nested
    class GetProductsByStatus {

        @Test
        void whenStatusProvided_thenReturnFilteredPaginatedDtos() {
            // Mock des produits retournés par le repository
            List<Product> mockProducts = IntStream.range(0, 10)
                    .mapToObj(i -> Product.builder()
                            .id((long) i)
                            .name("Product " + i)
                            .active(true)
                            .build())
                    .toList();

            Page<Product> mockPage = new PageImpl<>(mockProducts, PageRequest.of(0, 10), 20);

            when(productRepository.findByActive(eq(true), any(Pageable.class))).thenReturn(mockPage);
            when(productMapper.toDto(any(Product.class))).thenAnswer(invocation -> {
                Product product = invocation.getArgument(0);
                return ProductDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .active(product.getActive())
                        .build();
            });

            // Appel au service
            Pageable pageable = PageRequest.of(0, 10);
            Page<ProductDto> result = productService.getProductsByStatus(true, pageable);

            // Vérifications
            assertNotNull(result);
            assertEquals(10, result.getNumberOfElements());
            assertEquals(20, result.getTotalElements());
            assertEquals("Product 0", result.getContent().get(0).getName());

            verify(productRepository).findByActive(eq(true), eq(pageable));
        }
    }

    /**
     * Test class for validating the behavior of the getProductById method in ProductService.
     * This class contains nested test cases to verify the expected functionality of fetching
     * a product by its ID from the database and mapping it to a ProductDto.
     */
    @Nested
    class GetProductById {
        @Test
        void whenValidId_thenReturnDto() {
            Long id = 1L;
            Product product = new Product();
            ProductDto dto = new ProductDto();

            when(productRepository.findByIdAndActive(id)).thenReturn(Optional.of(product));
            when(productMapper.toDto(product)).thenReturn(dto);

            ProductDto result = productService.getProductById(id);

            assertThat(result).isEqualTo(dto);
            verify(productRepository).findByIdAndActive(id);
            verify(productMapper).toDto(product);
        }

        @Test
        void whenInvalidId_thenThrowException() {
            Long id = 1L;
            when(productRepository.findByIdAndActive(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.getProductById(id))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Le produit avec l'ID " + id + " est inactif ou n'existe pas.");
        }
    }

    /**
     * Test class for validating the behavior of the updateProduct method
     * in the ProductService component. This class covers the following scenarios:
     *
     * 1. Successfully updating an existing product with valid details.
     * 2. Throwing a NotFoundException if the provided product ID does not exist.
     *
     * These tests ensure that the update operation works correctly and handles
     * exceptions properly when updating a product.
     */
    @Nested
    class UpdateProduct {
        @Test
        void whenValidUpdate_thenReturnDto() {
            Long id = 1L;
            ProductDto inputDto = new ProductDto();
            Product existingProduct = new Product();
            Product updatedProduct = new Product();
            Product savedProduct = new Product();
            ProductDto outputDto = new ProductDto();

            when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
            when(productMapper.toEntity(inputDto)).thenReturn(updatedProduct);
            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
            when(productMapper.toDto(savedProduct)).thenReturn(outputDto);

            ProductDto result = productService.updateProduct(id, inputDto);

            assertThat(result).isEqualTo(outputDto);
            verify(productRepository).findById(id);
            verify(productMapper).toEntity(inputDto);
            verify(productRepository).save(any(Product.class));
            verify(productMapper).toDto(savedProduct);
        }

        @Test
        void whenInvalidId_thenThrowException() {
            Long id = 1L;
            ProductDto inputDto = new ProductDto();

            when(productRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.updateProduct(id, inputDto))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Ce produit n'existe pas");
        }
    }

    /**
     * Unit tests for the delete functionality of the product service.
     *
     * This class contains tests to validate the behavior of the deleteProduct method in the product service layer.
     */
    @Nested
    class DeleteProduct {
        @Test
        void whenValidId_thenDelete() {
            Long id = 1L;
            when(productRepository.findById(id)).thenReturn(Optional.of(new Product()));

            productService.deleteProduct(id);

            verify(productRepository).findById(id);
            verify(productRepository).deleteById(id);
        }

        @Test
        void whenInvalidId_thenThrowException() {
            Long id = 1L;
            when(productRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.deleteProduct(id))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Le produit avec l'ID " + id + " n'existe pas.");
        }
    }
}
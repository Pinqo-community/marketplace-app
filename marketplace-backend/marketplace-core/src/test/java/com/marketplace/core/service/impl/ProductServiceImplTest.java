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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

    @Nested
    class GetAvailableProducts {
        @Test
        void whenProductsExist_thenReturnDtos() {
            List<Product> products = List.of(new Product());
            List<ProductDto> dtos = List.of(new ProductDto());

            when(productRepository.findByActiveAndStockQuantityGreaterThan(true, 0)).thenReturn(products);
            when(productMapper.toDtoList(products)).thenReturn(dtos);

            List<ProductDto> result = productService.getAvailableProducts();

            assertThat(result).isEqualTo(dtos);
            verify(productRepository).findByActiveAndStockQuantityGreaterThan(true, 0);
            verify(productMapper).toDtoList(products);
        }
    }

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

    @Nested
    class GetProductsByStatus {
        @Test
        void whenStatusProvided_thenReturnFilteredDtos() {
            Boolean active = true;
            List<Product> products = List.of(new Product());
            List<ProductDto> dtos = List.of(new ProductDto());

            when(productRepository.findByActive(active)).thenReturn(products);
            when(productMapper.toDtoList(products)).thenReturn(dtos);

            List<ProductDto> result = productService.getProductsByStatus(active);

            assertThat(result).isEqualTo(dtos);
            verify(productRepository).findByActive(active);
            verify(productMapper).toDtoList(products);
        }
    }

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
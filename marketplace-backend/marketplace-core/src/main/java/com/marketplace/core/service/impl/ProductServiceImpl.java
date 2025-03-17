package com.marketplace.core.service.impl;

import com.marketplace.api.dto.product.ProductDto;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.api.service.ProductService;
import com.marketplace.core.entity.Product;
import com.marketplace.core.repository.ProductRepository;
import com.marketplace.core.utils.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Implementation of the ProductService interface, providing CRUD operations
 * for product management.
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Creates a new product entity based on the provided DTO.
     *
     * @param productDto The DTO containing the product details.
     * @return The saved product entity.
     */
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.atDebug().log("Enter createProduct(:productDto: {})", productDto);

        if (productDto.getMaxQuantityByPurchase() <= 0 || productDto.getMaxQuantityByPurchase() > productDto.getStockQuantity()){
            throw new IllegalArgumentException("La quantité maximale par achat doit être supérieure à 0 et inférieure ou égale à la quantité en stock");
        }

        Product product = productMapper.toEntity(productDto);
        log.info("Converted Product: {}", product);
        Product savedProduct = productRepository.save(product);
        ProductDto response = productMapper.toDto(savedProduct);

        log.atDebug().log("Leave createProduct() - return {}", response);

        return response;
    }


    /**
     * Retrieves a paginated list of available products. An available product is defined
     * as a product that is active and has a stock quantity greater than zero.
     *
     * @param pageable the pagination information, including page number, size, and sorting details
     * @return a paginated list of available products mapped to ProductDto objects
     */
    @Override
    public Page<ProductDto> getAvailableProducts(Pageable pageable) {
        log.info("Fetching available products with pagination: {}", pageable);

        Page<Product> pagedProducts = productRepository.findByActiveAndStockQuantityGreaterThan(true, 0, pageable);

        log.atDebug().log("Fetched {} products for pagination (page: {} size: {})",
                pagedProducts.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());

        return pagedProducts.map(productMapper::toDto);
    }


    /**
     * Retrieves a page of products filtered by their active status.
     *
     * @param active the active status of the products to filter by; true for active products, false for inactive products
     * @param pageable the pageable object containing pagination and sorting information
     * @return a page of ProductDto objects representing the filtered products
     */
    @Override
    public Page<ProductDto> getProductsByStatus(Boolean active, Pageable pageable) {

        log.info("Fetching products by status: {}, Page request: {}", active, pageable);

        Page<Product> productPage = productRepository.findByActive(active, pageable);

        log.atDebug().log("Fetched {} products with status '{}' for pagination (page: {}, size: {})",
                productPage.getTotalElements(),
                active,
                pageable.getPageNumber(),
                pageable.getPageSize());


        return productPage.map(productMapper::toDto);
    }


    /**
     * Retrieves a product entity by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the product entity if found.
     */
    @Override
    public ProductDto getProductById(Long id) {
        log.atDebug().log("Enter getProductById(:id: {})", id);

        Product product = productRepository.findByIdAndActive(id)
                .orElseThrow(() -> new NotFoundException("Le produit avec l'ID " + id + " est inactif ou n'existe pas."));
        ProductDto response = productMapper.toDto(product);

        log.atDebug().log("Leave getProductById() - return {}", response);

        return response;
    }

    /**
     * Updates an existing product entity with details from the provided DTO.
     * Throws ProductNotFoundException if the product is not found.
     *
     * @param id The ID of the product to update.
     * @param productDto The DTO containing the updated product details.
     * @return The updated product entity.
     */
    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        log.atDebug().log("Enter updateProduct(id: {}, productDto: {})", id, productDto);

        Product product = productRepository.findById(id)
                .map(existingProduct -> {

                    Product updatedProduct = productMapper.toEntity(productDto);
                    updatedProduct.setId(id);
                    return productRepository.save(updatedProduct);
                })
                .orElseThrow(() -> new NotFoundException("Ce produit n'existe pas"));

        ProductDto response = productMapper.toDto(product);

        log.atDebug().log("Leave updateProduct() - return {}", response);

        return response;
    }

    /**
     * Deletes a product entity by its ID.
     *
     * @param id The ID of the product to delete.
     */
    @Override
    public void deleteProduct(Long id) {
        log.atDebug().log("Enter deleteProduct(:id: {})", id);

        if (productRepository.findById(id).isEmpty()) {
            log.atError().log("Product with ID {} not found", id);
            throw new NotFoundException("Le produit avec l'ID " + id + " n'existe pas.");
        }
        productRepository.deleteById(id);

        log.atDebug().log("Leave deleteProduct()");
    }

}


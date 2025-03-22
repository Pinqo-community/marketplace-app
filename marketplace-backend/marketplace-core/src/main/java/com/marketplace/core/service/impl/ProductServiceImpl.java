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
     * Retrieves a paginated list of available products (active products with stock > 0).
     *
     * @param pageable The pagination information
     * @return A page of available ProductDto objects
     */
    @Override
    public Page<ProductDto> getAvailableProducts(Pageable pageable) {
        if (pageable == null) {
            log.atError().log("Pageable parameter cannot be null");
            throw new IllegalArgumentException("La page est requise");
        }

        log.atDebug().log("Retrieving available products with page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Product> productPage = productRepository.findByActiveAndStockQuantityGreaterThan(true, 0, pageable);

        log.atDebug().log("Found {} available products", productPage.getTotalElements());

        return productPage.map(productMapper::toDto);
    }

    /**
     * Retrieves a paginated list of products filtered by their active status.
     *
     * @param active   The active status to filter products by
     * @param pageable The pagination information
     * @return A page of ProductDto objects matching the specified active status
     */
    @Override
    public Page<ProductDto> getProductsByStatus(Boolean active, Pageable pageable) {
        if (pageable == null) {
            log.atError().log("Pageable parameter cannot be null");
            throw new IllegalArgumentException("La page est requise");
        }

        if (active == null) {
            log.atError().log("Active status parameter cannot be null"
            );
            throw new IllegalArgumentException("Le statut doit être spécifié");
        }

        log.atDebug().log("Retrieving products with active status={}, page={}, size={}",
                active, pageable.getPageNumber(), pageable.getPageSize());

        Page<Product> productPage = productRepository.findByActive(active, pageable);

        log.atDebug().log("Found {} products with active status={}",
                productPage.getTotalElements(), active);

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


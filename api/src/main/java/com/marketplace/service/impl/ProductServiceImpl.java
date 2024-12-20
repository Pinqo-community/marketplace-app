package com.marketplace.service.impl;

import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import com.marketplace.exception.NotFoundException;
import com.marketplace.exception.IllegalArgumentException;
import com.marketplace.repository.ProductRepository;
import com.marketplace.service.ProductService;
import com.marketplace.utils.mapper.ProductMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the ProductService interface, providing CRUD operations
 * for product management.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * Creates a new product entity based on the provided DTO.
     *
     * @param productDto The DTO containing the product details.
     * @return The saved product entity.
     */
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Received ProductDto: {}", productDto);

        if (productDto.getMaxQuantityByPurchase() <= 0 || productDto.getMaxQuantityByPurchase() > productDto.getStockQuantity()){
            throw new IllegalArgumentException("La quantité maximale par achat doit être supérieure à 0 et inférieure ou égale à la quantité en stock");
        }

        Product product = ProductMapper.INSTANCE.toEntity(productDto);
        log.info("Converted Product: {}", product);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.toDto(savedProduct);
    }

    /**
     * Retrieves all products with stock > 0 for customers.
     *
     * @return A list of available products.
     */
    @Override
    public List<Product> getAvailableProducts() {
        log.info("Retrieving products with stock > 0 and active status");

      return productRepository.findByActiveAndStockQuantityGreaterThan(true, 0);
    }


    /**
     * Retrieves a product entity by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the product entity if found.
     */
    @Override
    public Product getProductById(Long id) {
        log.info("Retrieving product with ID {}", id);
        log.info("Found product with ID {}", id);
          return productRepository.findByIdAndActive(id)
                .orElseThrow(() -> new NotFoundException("Le produit avec l'ID " + id + " est inactif ou n'existe pas."));
    }

    /**
     * Retrieves product entities from the database by status.
     *
     * @return A list of product .
     */
    @Override
    public List<ProductDto> getProductsByStatus(Boolean active) {
        log.info("Retrieving products with status {}", active);
        List<Product> products = productRepository.findByActive(active);
        log.info("Found {} products with status {}", products.size(), active);
        return ProductMapper.INSTANCE.toDtoList(products);
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
    public Product updateProduct(Long id, ProductDto productDto) {

        return productRepository.findById(id)
                .map(existingProduct -> {

                    Product updatedProduct = ProductMapper.INSTANCE.toEntity(productDto);
                    updatedProduct.setId(existingProduct.getId());
                    return productRepository.save(updatedProduct);
                })
                .orElseThrow(() -> new NotFoundException("Ce produit n'existe pas"));
    }

    /**
     * Deletes a product entity by its ID.
     *
     * @param id The ID of the product to delete.
     */
    @Override
    public void deleteProduct(Long id) {
        log.info("Attempting to delete product with ID {}", id);

        if (productRepository.findById(id).isEmpty()) {
            log.error("Product with ID {} not found", id);
            throw new NotFoundException("Le produit avec l'ID " + id + " n'existe pas.");
        }
            productRepository.deleteById(id);
            log.info("Product with ID {} deleted successfully", id);
    }
}



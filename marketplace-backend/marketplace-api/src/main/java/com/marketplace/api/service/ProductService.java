package com.marketplace.api.service;


import com.marketplace.api.dto.product.ProductDto;

import java.util.List;

/**
 * Interface defining CRUD operations for managing products.
 */

public interface ProductService {
    /**
     * Creates a new product based on the provided DTO.
     *
     * @param productDto Data Transfer Object containing product details.
     * @return The created product entity.
     */
    ProductDto createProduct(ProductDto productDto);


    /**
     * Retrieves all products with stock > 0 for customers.
     *
     * @return A list of available products.
     */
    List<ProductDto> getAvailableProducts();


    /**
     * Retrieves a product by its unique ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the product if found, or empty if not.
     */
    ProductDto getProductById(Long id);

    /**
     * Select products by status
     * @param active  product's status
     * @return A list of all products function  their status
     */
    List<ProductDto> getProductsByStatus(Boolean active);

    /**
     * Updates an existing product with the details provided in the DTO.
     *
     * @param id The ID of the product to update.
     * @param productDto Data Transfer Object containing updated product details.
     * @return The updated product entity.
     */
    ProductDto updateProduct(Long id, ProductDto productDto);

    /**
     * Deletes a product by its unique ID.
     *
     * @param id The ID of the product to delete.
     */
    void deleteProduct(Long id);

}

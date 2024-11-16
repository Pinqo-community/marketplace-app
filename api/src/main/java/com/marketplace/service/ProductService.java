package com.marketplace.service;

import com.marketplace.entity.Product;
import com.marketplace.dto.ProductDto;

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
     * Retrieves all products from the database.
     *
     * @return A list of all products.
     */
    List<Product> getAllProducts();

    /**
     * Retrieves a product by its unique ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the product if found, or empty if not.
     */
    Product getProductById(Long id);

    /**
     * Updates an existing product with the details provided in the DTO.
     *
     * @param id The ID of the product to update.
     * @param productDto Data Transfer Object containing updated product details.
     * @return The updated product entity.
     */
    Product updateProduct(Long id, ProductDto productDto);

    /**
     * Deletes a product by its unique ID.
     *
     * @param id The ID of the product to delete.
     */
    void deleteProduct(Long id);
}

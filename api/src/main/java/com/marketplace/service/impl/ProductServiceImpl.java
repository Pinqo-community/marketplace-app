package com.marketplace.service.impl;

import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import com.marketplace.exception.ProductNotFoundException;
import com.marketplace.repository.ProductRepository;
import com.marketplace.service.ProductService;
import com.marketplace.utils.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ProductService interface, providing CRUD operations
 * for product management.
 */
@Service
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
    public Product createProduct(ProductDto productDto) {
        Product product = ProductMapper.INSTANCE.toEntity(productDto);
        log.info("Product before save: " + product);
            return productRepository.save(product);
    }

    /**
     * Retrieves all product entities from the database.
     *
     * @return A list of all product entities.
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product entity by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the product entity if found.
     */
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Ce produit n'existe pas"));
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

                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setPhoto(updatedProduct.getPhoto());
                    existingProduct.setUnitPrice(updatedProduct.getUnitPrice());
                    existingProduct.setNutritionalValue(updatedProduct.getNutritionalValue());
                    existingProduct.setListOfIngredients(updatedProduct.getListOfIngredients());
                    existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
                    existingProduct.setCriticalLevel(updatedProduct.getCriticalLevel());

                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new ProductNotFoundException("Ce produit n'existe pas"));
    }

    /**
     * Deletes a product entity by its ID.
     *
     * @param id The ID of the product to delete.
     */
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}



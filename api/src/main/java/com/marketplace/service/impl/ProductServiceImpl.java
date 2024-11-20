package com.marketplace.service.impl;

import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import com.marketplace.exception.NotFoundException;
import com.marketplace.exception.UnavailableProductException;
import com.marketplace.repository.ProductRepository;
import com.marketplace.service.ProductService;
import com.marketplace.utils.mapper.ProductMapper;
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
        Product product = ProductMapper.INSTANCE.toEntity(productDto);
        log.info("Converted Product: {}", product);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.toDto(savedProduct);
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
        return productRepository.findById(id)
                .map(product -> {
                    if (!product.getActive()) {
                        throw new UnavailableProductException("Le produit avec l'ID " + id + " est inactif ou non disponible.");
                    }
                    if (product.getStockQuantity() == 0) {
                        throw new UnavailableProductException("Le produit est en rupture de stock.");
                    }
                    return product;
                })
                .orElseThrow(() -> new NotFoundException("Ce produit n'existe pas"));
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
                    existingProduct.setActive(updatedProduct.getActive());

                    return productRepository.save(existingProduct);
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
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Ce produit n'existe pas");
        }
        productRepository.deleteById(id);
    }


}



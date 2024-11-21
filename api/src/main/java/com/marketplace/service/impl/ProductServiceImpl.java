package com.marketplace.service.impl;

import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import com.marketplace.exception.NotFoundException;
import com.marketplace.exception.UnavailableProductException;
import com.marketplace.exception.IllegalArgumentException;
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
                    return product;
                })
                .orElseThrow(() -> new NotFoundException("Ce produit n'existe pas"));
    }

    /**
     * Retrieves product entities from the database by status.
     *
     * @return A list of product .
     */
    @Override
    public List<ProductDto> getProductsByStatus(Boolean active) {
        if (active == null) {
            throw new IllegalArgumentException("Le paramètre 'active' ne peut pas être null.");
        }

        List<Product> products = productRepository.findByActive(active);

        if (products.isEmpty()) {
            throw new NotFoundException("Aucun produit trouvé avec l'état actif = " + active);
        }
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
        if (!id.equals(productDto.getId())) {
            throw new IllegalArgumentException("L'Id de l'url ne correspond pas à l'Id du DTO");
        }

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
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Ce produit n'existe pas");
        }
        productRepository.deleteById(id);
    }


}



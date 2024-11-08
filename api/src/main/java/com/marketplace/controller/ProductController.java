package com.marketplace.controller;

import com.marketplace.dto.ProductDto;
import com.marketplace.entity.Product;
import com.marketplace.service.ProductService;
import com.marketplace.utils.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing products in the marketplace.
 * Provides endpoints for creating, retrieving, updating, and deleting products.
 */
@Tag(name = "Products", description = "Product management in the marketplace")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * Creates a new product in the marketplace.
     *
     * @param productDto The product data transfer object containing product details.
     * @return A ResponseEntity with the created product and HTTP status 201.
     */
    @PostMapping
    @Operation(summary = "Create a product", description = "Add a new product in the marketplace")
    @ApiResponse(responseCode = "201", description = "Successfully created product")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("POST /products - Creating a new product");
        Product product = convertToEntity(productDto);
        Product savedProduct = productService.createProduct(productDto);
        log.info("POST /products - Product created successfully");
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    /**
     * Retrieves all products available in the marketplace.
     *
     * @return A ResponseEntity with a list of products and HTTP status 200, or status 204 if no products found.
     */
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves a list of all products.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product list successfully retrieved."),
            @ApiResponse(responseCode = "204", description = "No products found.")
    })
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("GET /products - Retrieving all products");
        List<Product> products = productService.getAllProducts();
        log.info("GET /products - Retrieved {} products", products.size());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id The unique identifier of the product.
     * @return A ResponseEntity with the product and HTTP status 200, or status 404 if not found.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieves a product based on its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully recovered."),
            @ApiResponse(responseCode = "404", description = "No product found.")
    })
    public ResponseEntity<ProductDto> getProductById(
            @Parameter(description = "Unique product identifier", required = true) @PathVariable("id") Long id) {
            log.info("GET /products/{} - Retrieving product", id);
            Product product = productService.getProductById(id);
            return new ResponseEntity<>(ProductMapper.INSTANCE.toDto(product), HttpStatus.OK);
//            if (product.isPresent()) {
//                log.info("GET /products/{} - Product found", id);
//                return new ResponseEntity<>(product.get(), HttpStatus.OK);
//            } else {
//                log.warn("GET /products/{} - Product not found", id);
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
    }

    /**
     * Updates an existing product's information.
     *
     * @param id The unique identifier of the product to update.
     * @param productDto The product data transfer object containing updated product details.
     * @return A ResponseEntity with the updated product and HTTP status 200, or status 404 if not found.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Updates informations on an existing product.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully updated."),
            @ApiResponse(responseCode = "404", description = "No product found.")
    })
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "Unique product identifier", required = true)
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductDto productDto) {
        try {
            log.info("PUT /products/{} -  Updating product", id);

            Product savedProduct = productService.updateProduct(id, productDto);
            return new ResponseEntity<>(ProductMapper.INSTANCE.toDto(savedProduct), HttpStatus.OK);
        } finally {
            log.info("PUT /products/{} - END: Product updated successfully", id);
        }
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id The unique identifier of the product to delete.
     * @return A ResponseEntity with HTTP status 204 if the deletion was successful, or status 404 if not found.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product based on its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product sucessfull deleted"),
            @ApiResponse(responseCode = "404", description = "No product find")
    })
    public ResponseEntity<HttpStatus> deleteProduct(
            @Parameter(description = "Unique product identifier", required = true)
            @PathVariable("id") Long id) {
                log.info("DELETE /products/{} - Deleting product", id);
                productService.deleteProduct(id);
                log.info("DELETE /products/{} - Product deleted successfully", id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Product convertToEntity(ProductDto productDto) {
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .photo(productDto.getPhoto())
                .unitPrice(productDto.getUnitPrice())
                .nutritionalValue(productDto.getNutritionalValue())
                .listOfIngredients(productDto.getListOfIngredients())
                .stockQuantity(productDto.getStockQuantity())
                .criticalLevel(productDto.getCriticalLevel())
                .build();
    }
}

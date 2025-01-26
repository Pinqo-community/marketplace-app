package com.marketplace.web.controller;


import com.marketplace.api.dto.product.ProductDto;
import com.marketplace.api.service.ProductService;
import com.marketplace.core.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto productDto) {
        try {
            log.info("POST /products - START: Creating a new product");
            ProductDto savedProductDto = productService.createProduct(productDto);
            log.info("POST /products - Product created successfully");
            return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
        } finally {
            log.info("POST /products - END: new product created");
        }
    }


    @GetMapping("/available")
    @Operation(summary = "Get available products", description = "Retrieve products with stock > 0 and active = true for customers.")
    @ApiResponse(responseCode = "200", description = "Available products retrieved successfully.")
    public ResponseEntity<List<ProductDto>> getAvailableProducts() {
        try {
            log.atInfo().log("GET /products - START: Retrieving products");

            return ResponseEntity.ok(productService.getAvailableProducts());
        } finally {
            log.atInfo().log("GET /products/available - DONE");
        }
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
            @ApiResponse(responseCode = "404", description = "No product found."),
            @ApiResponse(responseCode = "410", description = "Product is no longer available."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<ProductDto> getProductById(
            @Parameter(description = "Unique product identifier", required = true) @PathVariable("id") Long id) {
        try {
            log.info("GET /products/{} - START: Retrieving product", id);
            return ResponseEntity.ok(productService.getProductById(id));
        } finally {
            log.info("GET /products/{} - END: product recovered", id);
        }
    }

    @Operation(summary = "Retrieve products by status", description = "Retrieve products filtered by their active/inactive status for producers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
                    @ApiResponse(responseCode = "400", description = "Invalid input parameter")
            })
    @GetMapping("/status")
    public ResponseEntity<List<ProductDto>> getProductsByStatus(@RequestParam(name = "active", required = true) Boolean active) {
        try {
            log.info("GET /products/status?active={} - START: Retrieving products", active);
            List<ProductDto> products = productService.getProductsByStatus(active);
            return ResponseEntity.ok(products);
        } finally {
            log.info("GET /products/status?active={} - END: products successfully retrieved", active);
        }
    }

    /**
     * Updates an existing product's information.
     *
     * @param id         The unique identifier of the product to update.
     * @param productDto The product data transfer object containing updated product details.
     * @return A ResponseEntity with the updated product and HTTP status 200, or status 404 if not found.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "The ID in the URL is mandatory and overrides any ID provided in the request body.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully updated."),
            @ApiResponse(responseCode = "404", description = "No product found.")
    })
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "Unique product identifier", required = true)
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductDto productDto) {
        try {
            log.info("PUT /products/{} - START:  Updating product", id);
            return ResponseEntity.ok(productService.updateProduct(id, productDto));
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
            @ApiResponse(responseCode = "204", description = "Product successfully deleted"),
            @ApiResponse(responseCode = "404", description = "No product found")
    })
    public ResponseEntity<HttpStatus> deleteProduct(
            @Parameter(description = "Unique product identifier", required = true)
            @PathVariable("id") Long id) {
        try {
            log.info("DELETE /products/{} - Deleting product", id);
            log.info("Controller: Attempting to delete product with ID {}", id);
            productService.deleteProduct(id);
            log.info("DELETE /products/{} - Product deleted successfully", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } finally {
            log.info("DELETE /products/{} - DONE", id);
        }

    }
}


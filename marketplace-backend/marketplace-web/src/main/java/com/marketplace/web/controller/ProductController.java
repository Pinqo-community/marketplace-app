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
 * REST controller for product management operations.
 */
@Tag(name = "Products", description = "Product management in the marketplace")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * Creates a new product.
     *
     * @param productDto product details
     * @return ResponseEntity with created product
     */
    @PostMapping
    @Operation(summary = "Create a product", description = "Add a new product in the marketplace")
    @ApiResponse(responseCode = "201", description = "Successfully created product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto productDto) {
        try {
            log.atInfo().log("POST /products - START");
            ProductDto savedProductDto = productService.createProduct(productDto);
            log.atInfo().log("POST /products - Product created successfully");
            return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
        } finally {
            log.atInfo().log("POST /products - END");
        }
    }

    /**
     * Retrieve a list of 8 available products
     *
     * @return ResponseEntity with list of  8 available products
     */
    @GetMapping("/homePage")
    @Operation(summary = " Get available products for home page", description ="Retrieve and display 8 products with stock > 0 and active = true for customers " )
    @ApiResponse(responseCode = "200", description = "8 available products displayed")
    public ResponseEntity<List<ProductDto>> getHomePageProducts() {
        try {
            log.atInfo().log("GET /products/homePage - START");
            List<ProductDto> products = productService.getAvailableProductsForHomePage();
            return ResponseEntity.ok(products);
        } finally {
            log.atInfo().log("GET /products/homePage - END");
        }
    }


    /**
     * Retrieves all available products.
     *
     * @return ResponseEntity with list of available products
     */
    @GetMapping("/available")
    @Operation(summary = "Get available products", description = "Retrieve products with stock > 0 and active = true for customers.")
    @ApiResponse(responseCode = "200", description = "Available products retrieved successfully.")
    public ResponseEntity<List<ProductDto>> getAvailableProducts() {
        try {
            log.atInfo().log("GET /products/available - START");
            return ResponseEntity.ok(productService.getAvailableProducts());
        } finally {
            log.atInfo().log("GET /products/available - END");
        }
    }

    /**
     * Retrieves product by ID.
     *
     * @param id product identifier
     * @return ResponseEntity with found product
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
            log.atInfo().log("GET /products/{} - START", id);
            return ResponseEntity.ok(productService.getProductById(id));
        } finally {
            log.atInfo().log("GET /products/{} - END", id);
        }
    }

    /**
     * Retrieves products by active status.
     *
     * @param active status filter
     * @return ResponseEntity with filtered products
     */
    @GetMapping("/status")
    @Operation(summary = "Retrieve products by status", description = "Retrieve products filtered by their active/inactive status for producers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter")
    })
    public ResponseEntity<List<ProductDto>> getProductsByStatus(@RequestParam(name = "active", required = true) Boolean active) {
        try {
            log.atInfo().log("GET /products/status?active={} - START", active);
            List<ProductDto> products = productService.getProductsByStatus(active);
            return ResponseEntity.ok(products);
        } finally {
            log.atInfo().log("GET /products/status?active={} - END", active);
        }
    }

    /**
     * Updates product by ID.
     *
     * @param id product identifier
     * @param productDto updated product details
     * @return ResponseEntity with updated product
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "The ID in the URL is mandatory and overrides any ID provided in the request body.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully updated."),
            @ApiResponse(responseCode = "404", description = "No product found.")
    })
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "Unique product identifier", required = true) @PathVariable("id") Long id,
            @Valid @RequestBody ProductDto productDto) {
        try {
            log.atInfo().log("PUT /products/{} - START", id);
            return ResponseEntity.ok(productService.updateProduct(id, productDto));
        } finally {
            log.atInfo().log("PUT /products/{} - END", id);
        }
    }

    /**
     * Deletes product by ID.
     *
     * @param id product identifier
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product based on its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product successfully deleted"),
            @ApiResponse(responseCode = "404", description = "No product found")
    })
    public ResponseEntity<HttpStatus> deleteProduct(
            @Parameter(description = "Unique product identifier", required = true) @PathVariable("id") Long id) {
        try {
            log.atInfo().log("DELETE /products/{} - START", id);
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } finally {
            log.atInfo().log("DELETE /products/{} - END", id);
        }
    }
}


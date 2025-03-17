package com.marketplace.web.controller;


import com.marketplace.api.dto.product.ProductDto;
import com.marketplace.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


/**
 * ProductController is responsible for handling API requests related to product management
 * within the marketplace. It exposes endpoints for creating, retrieving, updating, and
 * deleting products as well as querying products based on specific criteria.
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
     * @param productDto the product data provided in the request body to create a new product
     * @return the created product details wrapped in a ResponseEntity with HTTP status 201 (Created)
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
     * Retrieves a paginated list of available products that have stock greater than zero
     * and are marked as active.
     *
     * @param page the page number to retrieve, defaults to 0 if not provided.
     * @param size the number of products per page, defaults to 8 if not provided.
     * @return a ResponseEntity containing a paginated list of ProductDto objects representing the available products.
     */
    @GetMapping("/available")
    @Operation(summary = "Get available products",
            description = "Retrieve paginated products with stock > 0 and active = true for customers.")
    @ApiResponse(responseCode = "200", description = "Paginated available products retrieved successfully.")
    public ResponseEntity<Page<ProductDto>> getAvailableProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        Pageable pageable = PageRequest.of(page, size);


        log.info("Request received to fetch paginated available products: page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<ProductDto> availableProducts = productService.getAvailableProducts(pageable);

        log.info("Returning {} available products for page {} with size {}", availableProducts.getContent().size(), pageable.getPageNumber(), pageable.getPageSize());

        return ResponseEntity.ok(availableProducts);
    }


    /**
     * Retrieves a paginated list of products filtered by their active/inactive status.
     *
     * @param active the status of the products to filter by; true for active products, false for inactive products (required).
     * @param page the page number to retrieve, with a 0-based index (optional, defaults to 0).
     * @param size the number of elements per page to retrieve (optional, defaults to 8).
     * @return a ResponseEntity containing a paginated list of ProductDto objects matching the specified status.
     */
    @GetMapping("/status")
    @Operation(summary = "Retrieve products by status", description = "Retrieve products filtered by their active/inactive status for producers.",
    parameters = {
        @Parameter(name = "active", description = "Product status (true for active, false for inactive)", required = true),
        @Parameter(name = "page", description = "Page number (0-based index)"),
        @Parameter(name = "size", description = "Number of elements per page")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated products retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter."),
    })

    public ResponseEntity<Page<ProductDto>> getProductsByStatus(
            @RequestParam(name = "active") Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

            log.info("Request received to fetch products by status: {}, with pagination: page {}, size {}", active, page, size);
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDto> productsByStatus = productService.getProductsByStatus(active, pageable);
            log.info("Returning {} products for status: {}, page: {}, size: {}", productsByStatus.getNumberOfElements(), active, page, size);
            return ResponseEntity.ok(productsByStatus);
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


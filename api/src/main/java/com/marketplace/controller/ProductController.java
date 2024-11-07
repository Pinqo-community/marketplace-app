package com.marketplace.controller;


import com.marketplace.entity.Product;
import com.marketplace.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Tag(name = "Products", description = "Product management in the marketplace")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a product", description = "Add a new product in the marketplace")
    @ApiResponse(responseCode = "201", description = "Successfully created product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves a list of all products.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product list successfully retrieved."),
            @ApiResponse(responseCode = "204", description = "No products found.")
    })
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieves a product based on its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully recovered."),
            @ApiResponse(responseCode = "404", description = "No product found.")
    })
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "Unique product identifier", required = true) @PathVariable("id") Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Updates informations on an existing product.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully updated."),
            @ApiResponse(responseCode = "404", description = "No product found.")
    })
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Unique product identifier", required = true) @PathVariable("id") Long id,
            @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product based on its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product sucessfull deleted"),
            @ApiResponse(responseCode = "404", description = "No product find")
    })
    public ResponseEntity<HttpStatus> deleteProduct(
            @Parameter(description = "Unique product identifier", required = true) @PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

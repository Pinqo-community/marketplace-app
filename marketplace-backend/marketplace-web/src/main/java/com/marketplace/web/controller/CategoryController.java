package com.marketplace.web.controller;

import com.marketplace.api.dto.category.CategoryCreateDto;
import com.marketplace.api.dto.category.CategoryResponseDto;
import com.marketplace.api.dto.category.CategoryUpdateDto;
import com.marketplace.api.dto.exception.ExceptionResponse;
import com.marketplace.api.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
 * REST controller for managing category operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Category management API")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Retrieves all categories.
     *
     * @return ResponseEntity containing list of categories
     */
    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "Retrieves a list of all available categories"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categories retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        try {
            log.atInfo().log("GET /categories - START: Retrieving all categories");
            return ResponseEntity.ok(categoryService.findAll());
        } finally {
            log.atInfo().log("GET /categories - END");
        }
    }

    /**
     * Retrieves a category by ID.
     *
     * @param id category identifier
     * @return ResponseEntity containing the found category
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get category by ID",
            description = "Retrieves a specific category by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category found successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    public ResponseEntity<CategoryResponseDto> getCategoryById(
            @Parameter(description = "Category unique identifier", required = true)
            @PathVariable("id") Long id) {
        try {
            log.atInfo().log("GET /categories/{} - START: Retrieving category", id);
            return ResponseEntity.ok(categoryService.findById(id));
        } finally {
            log.atInfo().log("GET /categories/{} - END", id);
        }
    }

    /**
     * Creates a new category.
     *
     * @param categoryCreateDto category creation data
     * @return ResponseEntity containing the created category
     */
    @PostMapping
    @Operation(
            summary = "Create category",
            description = "Creates a new category with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Category already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        try {
            log.atInfo().log("POST /categories - START: Creating new category");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(categoryService.create(categoryCreateDto));
        } finally {
            log.atInfo().log("POST /categories - END");
        }
    }

    /**
     * Updates an existing category.
     *
     * @param id category identifier
     * @param categoryUpdateDto category update data
     * @return ResponseEntity containing the updated category
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update category",
            description = "Updates an existing category with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @Parameter(description = "Category unique identifier", required = true)
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {
        try {
            log.atInfo().log("PUT /categories/{} - START: Updating category", id);
            return ResponseEntity.ok(categoryService.update(categoryUpdateDto, id));
        } finally {
            log.atInfo().log("PUT /categories/{} - END", id);
        }
    }

    /**
     * Deletes a category.
     *
     * @param id category identifier
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete category",
            description = "Removes an existing category by its identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Category deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category unique identifier", required = true)
            @PathVariable("id") Long id) {
        try {
            log.atInfo().log("DELETE /categories/{} - START: Deleting category", id);
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } finally {
            log.atInfo().log("DELETE /categories/{} - END", id);
        }
    }
}

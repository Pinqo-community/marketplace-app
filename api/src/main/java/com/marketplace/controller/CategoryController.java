package com.marketplace.controller;

import com.marketplace.dto.ExceptionResponse;
import com.marketplace.dto.category.CategoryCreateDto;
import com.marketplace.dto.category.CategoryResponseDto;
import com.marketplace.dto.category.CategoryUpdateDto;
import com.marketplace.service.impl.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "Categories", description = "API for categories")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Find a list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all categories",
                    content = @Content(mediaType = "application/json",
                              array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDto.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                              schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        try {
            log.info("GET /categories - START");
            return ResponseEntity.ok(categoryService.findAll());
        } finally {
            log.info("GET /categories - DONE");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Find the category with the given ID")
    @Parameter(name="id", description = "The ID of the category you want to find")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found with this id",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable("id") Long id) {
        try {
            log.info("GET /categories/{id} - START");
            return ResponseEntity.ok(categoryService.findById(id));
        } finally {
            log.info("GET /categories/{id} - DONE");
        }
    }

    @PostMapping
    @Operation(summary = "Create category", description = "Add a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Category with this name already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryCreateDto categoryCreateDto) {
        try {
            log.info("POST /categories - START");
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(categoryCreateDto));
        } finally {
            log.info("POST /categories - DONE");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update an existing category with a given ID")
    @Parameter(name="id", description = "The ID of the category you want to update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found with this id",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryUpdateDto categoryUpdateDto) {
        try {
            log.info("PUT /categories/{id} - START");
            return ResponseEntity.ok(categoryService.update(categoryUpdateDto, id));
        } finally {
            log.info("PUT /categories/{id} - DONE");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete the category with the given ID")
    @Parameter(name="id", description = "The ID of the category you want to delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category successfully deleted",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Category not found with this id",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        try {
            log.info("DELETE /categories/{id} - START");
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } finally {
            log.info("DELETE /categories/{id} - DONE");
        }
    }
}

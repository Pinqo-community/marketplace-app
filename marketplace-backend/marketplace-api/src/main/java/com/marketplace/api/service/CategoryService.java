package com.marketplace.api.service;

import com.marketplace.api.dto.category.CategoryCreateDto;
import com.marketplace.api.dto.category.CategoryResponseDto;
import com.marketplace.api.dto.category.CategoryUpdateDto;

import java.util.List;

/**
 * Service interface for managing category operations.
 * Provides CRUD functionality for categories.
 */
public interface CategoryService {

    /**
     * Retrieves all categories.
     *
     * @return List of all categories as CategoryResponseDto objects
     */
    List<CategoryResponseDto> findAll();

    /**
     * Retrieves a category by its ID.
     *
     * @param id unique identifier of the category
     * @return CategoryResponseDto representing the found category
     */
    CategoryResponseDto findById(Long id);

    /**
     * Creates a new category.
     *
     * @param categoryCreateDto DTO containing category creation data
     * @return CategoryResponseDto of the created category
     */
    CategoryResponseDto create(CategoryCreateDto categoryCreateDto);

    /**
     * Updates an existing category.
     *
     * @param categoryUpdateDto DTO containing category update data
     * @param id unique identifier of the category to update
     * @return CategoryResponseDto of the updated category
     */
    CategoryResponseDto update(CategoryUpdateDto categoryUpdateDto, Long id);

    /**
     * Deletes a category by its ID.
     *
     * @param id unique identifier of the category to delete
     */
    void deleteById(Long id);
}

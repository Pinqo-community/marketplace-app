package com.marketplace.core.service.impl;

import com.marketplace.api.dto.category.CategoryCreateDto;
import com.marketplace.api.dto.category.CategoryResponseDto;
import com.marketplace.api.dto.category.CategoryUpdateDto;
import com.marketplace.api.exception.AlreadyExistsException;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.api.service.CategoryService;
import com.marketplace.core.entity.Category;
import com.marketplace.core.repository.CategoryRepository;
import com.marketplace.core.utils.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Implementation of CategoryService interface for managing category operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Retrieves all categories.
     *
     * @return list of category response DTOs
     */
    public List<CategoryResponseDto> findAll() {
        log.atDebug().log("Enter findAll()");

        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDto> response = categoryMapper.toResponseList(categories);

        log.atDebug().log("Leave findAll() - return {}", response);

        return response;
    }

    /**
     * Retrieves category by ID.
     *
     * @param id category identifier
     * @return category response DTO
     * @throws NotFoundException if category not found
     */
    public CategoryResponseDto findById(Long id) {
        log.atDebug().log("Enter findById(id={})", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));

        CategoryResponseDto response = categoryMapper.toResponse(category);

        log.atDebug().log("Leave findById() - return {}", response);

        return response;
    }

    /**
     * Creates a new category.
     *
     * @param categoryCreateDto category creation data
     * @return created category response DTO
     * @throws AlreadyExistsException if category name already exists
     */
    public CategoryResponseDto create(CategoryCreateDto categoryCreateDto) {
        log.atDebug().log("Enter create(categoryCreateDto={})", categoryCreateDto);

        if (categoryRepository.existsByNameIgnoreCase(categoryCreateDto.name())) {
            throw new AlreadyExistsException("Il existe déja une catégorie avec ce nom : " + categoryCreateDto.name());
        }

        Category category = Category.builder()
                .name(categoryCreateDto.name())
                .build();

        Category categorySaved = categoryRepository.save(category);
        CategoryResponseDto response = categoryMapper.toResponse(categorySaved);

        log.atDebug().log("Leave create() - return {}", response);

        return response;
    }

    /**
     * Updates an existing category.
     *
     * @param categoryUpdateDto category update data
     * @param id category identifier
     * @return updated category response DTO
     * @throws NotFoundException if category not found
     */
    public CategoryResponseDto update(CategoryUpdateDto categoryUpdateDto, Long id) {
        log.atDebug().log("Enter update(categoryUpdateDto={}, id={})", categoryUpdateDto, id);

        Category categoryUpdated = categoryRepository.findById(id)
                .map(category -> {
                    if (!categoryUpdateDto.name().isEmpty()) {
                        category.setName(categoryUpdateDto.name());
                    }
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));

        CategoryResponseDto response = categoryMapper.toResponse(categoryUpdated);

        log.atDebug().log("Leave update() - return {}", response);

        return response;
    }

    /**
     * Deletes a category by ID.
     *
     * @param id category identifier
     * @throws NotFoundException if category not found
     */
    public void deleteById(Long id) {
        log.atDebug().log("Enter deleteById(id={})", id);

        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));

        categoryRepository.deleteById(id);

        log.atDebug().log("Leave deleteById()");
    }
}


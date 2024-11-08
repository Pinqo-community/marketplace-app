package com.marketplace.service.impl;

import com.marketplace.dto.category.CategoryCreateDto;
import com.marketplace.dto.category.CategoryResponseDto;
import com.marketplace.dto.category.CategoryUpdateDto;
import java.util.List;

public interface CategoryServiceInterface {
    List<CategoryResponseDto> findAll();
    CategoryResponseDto findById(Long id);
    CategoryResponseDto create(CategoryCreateDto categoryCreateDto);
    CategoryResponseDto update(CategoryUpdateDto categoryUpdateDto, Long id);
    void deleteById(Long id);
}

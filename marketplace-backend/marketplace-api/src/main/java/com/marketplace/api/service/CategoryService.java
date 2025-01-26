package com.marketplace.api.service;

import com.marketplace.api.dto.category.CategoryCreateDto;
import com.marketplace.api.dto.category.CategoryResponseDto;
import com.marketplace.api.dto.category.CategoryUpdateDto;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAll();
    CategoryResponseDto findById(Long id);
    CategoryResponseDto create(CategoryCreateDto categoryCreateDto);
    CategoryResponseDto update(CategoryUpdateDto categoryUpdateDto, Long id);
    void deleteById(Long id);
}

package com.marketplace.service;

import com.marketplace.dto.category.CategoryCreateDto;
import com.marketplace.dto.category.CategoryUpdateDto;
import com.marketplace.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category create(CategoryCreateDto categoryCreateDto);
    Category update(CategoryUpdateDto categoryUpdateDto, Long id);
    void deleteById(Long id);
}

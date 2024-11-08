package com.marketplace.service.impl;

import com.marketplace.dto.category.CategoryCreateDto;
import com.marketplace.dto.category.CategoryResponseDto;
import com.marketplace.dto.category.CategoryUpdateDto;
import com.marketplace.entity.Category;
import com.marketplace.exception.AlreadyExistsException;
import com.marketplace.exception.NotFoundException;
import com.marketplace.repository.CategoryRepository;
import com.marketplace.utils.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponseDto> findAll() {
        return categoryMapper.toResponseList(categoryRepository.findAll());
    }

    public CategoryResponseDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));
        return categoryMapper.toResponse(category);
    }

    public CategoryResponseDto create(CategoryCreateDto categoryCreateDto) {
        if (categoryRepository.existsByName(categoryCreateDto.getName())) {
                throw new AlreadyExistsException("Il existe déja une catégorie avec ce nom : " + categoryCreateDto.getName());
        }
        Category category = categoryMapper.toEntity(categoryCreateDto);
        Category createdCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(createdCategory);
    }

    public CategoryResponseDto update(CategoryUpdateDto categoryUpdateDto, Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));
        categoryMapper.updateCategoryFromDto(categoryUpdateDto, category);
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }

    public void deleteById(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));
        categoryRepository.deleteById(id);
    }
}


package com.marketplace.service.impl;

import com.marketplace.dto.category.CategoryCreateDto;
import com.marketplace.dto.category.CategoryUpdateDto;
import com.marketplace.entity.Category;
import com.marketplace.exception.AlreadyExistsException;
import com.marketplace.exception.NotFoundException;
import com.marketplace.repository.CategoryRepository;
import com.marketplace.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));
    }

    public Category create(CategoryCreateDto categoryCreateDto) {
        if (categoryRepository.existsByName(categoryCreateDto.name())) {
                throw new AlreadyExistsException("Il existe déja une catégorie avec ce nom : " + categoryCreateDto.name());
        }

        Category category = Category.builder()
                .name(categoryCreateDto.name())
                .build();

        return categoryRepository.save(category);
    }

    public Category update(CategoryUpdateDto categoryUpdateDto, Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    if (!categoryUpdateDto.name().isEmpty()) {
                        category.setName(categoryUpdateDto.name());
                    }
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));
    }

    public void deleteById(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de catégorie avec cet ID : " + id));

        categoryRepository.deleteById(id);
    }
}


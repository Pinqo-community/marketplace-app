package com.marketplace.service.impl;

import com.marketplace.entity.Category;
import com.marketplace.exception.AlreadyExistsException;
import com.marketplace.exception.NotFoundException;
import com.marketplace.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + id));
    }

    public Category save(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistsException("Category already exists with name: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + id));
        categoryRepository.deleteById(id);
    }
}


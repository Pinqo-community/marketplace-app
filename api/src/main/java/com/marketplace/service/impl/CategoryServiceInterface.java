package com.marketplace.service.impl;

import com.marketplace.entity.Category;
import java.util.List;

public interface CategoryServiceInterface {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    void deleteById(Long id);
}

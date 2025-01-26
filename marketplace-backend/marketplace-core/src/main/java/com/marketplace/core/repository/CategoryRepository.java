package com.marketplace.core.repository;

import com.marketplace.core.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository <Category,Long> {
    boolean existsByNameIgnoreCase(String name);
}

package com.marketplace.repository;

import com.marketplace.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository <Category,Long> {
    boolean existsByName(String name);
}

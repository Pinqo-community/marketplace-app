package com.marketplace.core.repository;

import com.marketplace.core.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository <Product, Long>{

    List<Product> findByActive(Boolean active);
    List<Product> findByActiveAndStockQuantityGreaterThan(boolean active, int stockQuantity);

    Page<Product> findByActiveAndStockQuantityGreaterThan(boolean active, int stockQuantity, Pageable pageable);
    Page<Product> findByActive(Boolean active, Pageable pageable);



    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.active = true")
    Optional<Product> findByIdAndActive(@Param("id") Long id);

}

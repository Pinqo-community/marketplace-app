package com.marketplace.repository;

import com.marketplace.entity.Product;
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

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.active = true")
    Optional<Product> findByIdAndActive(@Param("id") Long id);

}

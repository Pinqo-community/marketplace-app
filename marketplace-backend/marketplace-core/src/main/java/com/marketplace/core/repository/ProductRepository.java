package com.marketplace.core.repository;

import com.marketplace.core.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository <Product, Long>{

    /**
     * Finds a page of products that are active and have a stock quantity greater than the specified value.
     *
     * @param active a boolean value indicating whether the product is active.
     * @param stockQuantity an integer specifying the minimum stock quantity.
     * @param pageable a Pageable object for pagination and sorting information.
     * @return a Page of Product objects matching the criteria.
     */
    Page<Product> findByActiveAndStockQuantityGreaterThan(boolean active, int stockQuantity, Pageable pageable);


    /**
     * Finds a page of products based on their active status.
     *
     * @param active a Boolean indicating whether the product is active.
     * @param pageable a Pageable object that specifies the pagination and sorting information.
     * @return a Page of Product objects that match the active status.
     */
    Page<Product> findByActive(Boolean active, Pageable pageable);



    /**
     * Retrieves a product by its ID if the product is active.
     *
     * @param id the ID of the product to be retrieved.
     * @return an Optional containing the Product if found and active, or an empty Optional if not.
     */
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.active = true")
    Optional<Product> findByIdAndActive(@Param("id") Long id);

}

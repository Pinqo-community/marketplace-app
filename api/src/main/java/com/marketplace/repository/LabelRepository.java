package com.marketplace.repository;

import com.marketplace.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {

    boolean existsByNameIgnoreCase(String name);


}

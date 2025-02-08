package com.marketplace.core.repository;

import com.marketplace.core.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {

    boolean existsByNameIgnoreCase(String name);


}

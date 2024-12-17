package com.marketplace.repository;

import com.marketplace.entity.Label;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabelRepository extends JpaRepository<Label, Long> {

    boolean existsByNameIgnoreCase(String name);

    @NonNull
    List<Label> findAll();

}

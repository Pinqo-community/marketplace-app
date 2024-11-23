package com.marketplace.repository;

import com.marketplace.entity.Role;
import com.marketplace.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(RoleType name);

    Optional<Role> findByName(RoleType name);
}

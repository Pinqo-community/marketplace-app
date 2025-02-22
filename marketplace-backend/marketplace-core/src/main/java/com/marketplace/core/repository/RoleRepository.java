package com.marketplace.core.repository;

import com.marketplace.core.entity.Role;
import com.marketplace.core.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(RoleType name);

    Optional<Role> findByName(RoleType name);
}

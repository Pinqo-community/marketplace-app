package com.marketplace.core.utils.mapper;

import com.marketplace.core.entity.Role;
import com.marketplace.core.entity.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleMapperTest {

    private RoleMapper roleMapper;

    @BeforeEach
    void setUp() {
        roleMapper = new RoleMapperImpl();
    }

    @Test
    void whenValidRole_thenMapToString() {
        // Given
        Role role = new Role();
        role.setName(RoleType.ROLE_USER);

        // When
        String result = roleMapper.toDto(role);

        // Then
        assertThat(result)
                .isNotNull()
                .isEqualTo("ROLE_USER");
    }

    @Test
    void whenNullRole_thenReturnNull() {
        // When
        String result = roleMapper.toDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void whenRoleWithNullName_thenReturnNull() {
        // Given
        Role role = new Role();
        role.setName(null);

        // When
        String result = roleMapper.toDto(role);

        // Then
        assertThat(result).isNull();
    }
}
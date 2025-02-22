package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.core.entity.Role;
import com.marketplace.core.entity.User;
import com.marketplace.core.entity.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private RoleMapper roleMapper;

    @BeforeEach
    void setUp() {
        roleMapper = new RoleMapperImpl();
        // MapStruct va générer une implémentation avec le suffixe "Impl"
        userMapper = new UserMapperImpl();
        ReflectionTestUtils.setField(userMapper, "roleMapper", roleMapper);
    }

    @Test
    void whenValidUser_thenMapToDto() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setEnabled(true);
        user.setProvider("local");

        Role role = new Role();
        role.setName(RoleType.ROLE_USER);
        user.setRoles(Set.of(role));

        // When
        UserDTO result = userMapper.toDto(user);

        // Then
        assertThat(result)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.id()).isEqualTo(1L);
                    assertThat(dto.email()).isEqualTo("test@test.com");
                    assertThat(dto.password()).isEqualTo("encodedPassword");
                    assertThat(dto.enabled()).isTrue();
                    assertThat(dto.provider()).isEqualTo("local");
                    assertThat(dto.roles())
                            .hasSize(1)
                            .contains("ROLE_USER");
                });
    }

    @Test
    void whenNullUser_thenReturnNull() {
        // When
        UserDTO result = userMapper.toDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void whenUserWithNullFields_thenMapWithNullValues() {
        // Given
        User user = new User();
        user.setId(1L);
        // Laissons les autres champs à null

        // When
        UserDTO result = userMapper.toDto(user);

        // Then
        assertThat(result)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.id()).isEqualTo(1L);
                    assertThat(dto.email()).isNull();
                    assertThat(dto.password()).isNull();
                    assertThat(dto.enabled()).isFalse();
                    assertThat(dto.provider()).isNull();
                    assertThat(dto.roles()).isEmpty();
                });
    }

    @Test
    void whenUserWithEmptyRoles_thenMapWithEmptyRoles() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setEnabled(true);
        user.setProvider("local");
        user.setRoles(Collections.emptySet());

        // When
        UserDTO result = userMapper.toDto(user);

        // Then
        assertThat(result)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.id()).isEqualTo(1L);
                    assertThat(dto.email()).isEqualTo("test@test.com");
                    assertThat(dto.password()).isEqualTo("encodedPassword");
                    assertThat(dto.enabled()).isTrue();
                    assertThat(dto.provider()).isEqualTo("local");
                    assertThat(dto.roles()).isEmpty();
                });
    }

    @Test
    void whenUserWithMultipleRoles_thenMapAllRoles() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setEnabled(true);
        user.setProvider("local");

        Role roleUser = new Role();
        roleUser.setName(RoleType.ROLE_USER);
        Role roleAdmin = new Role();
        roleAdmin.setName(RoleType.ROLE_ADMIN);

        user.setRoles(Set.of(roleUser, roleAdmin));

        // When
        UserDTO result = userMapper.toDto(user);

        // Then
        assertThat(result)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.id()).isEqualTo(1L);
                    assertThat(dto.email()).isEqualTo("test@test.com");
                    assertThat(dto.password()).isEqualTo("encodedPassword");
                    assertThat(dto.enabled()).isTrue();
                    assertThat(dto.provider()).isEqualTo("local");
                    assertThat(dto.roles())
                            .hasSize(2)
                            .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
                });
    }
}
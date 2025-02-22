package com.marketplace.core.utils.mapper;

import com.marketplace.core.entity.Role;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting Role entities to string representations.
 * Uses MapStruct for automatic mapping implementation.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {
    /**
     * Converts a Role entity to its string representation.
     * Returns null if the role or role name is null.
     *
     * @param role the role entity to convert
     * @return string representation of the role name, or null if role/name is null
     */
    default String toDto(Role role) {
        if (role == null || role.getName() == null) {
            return null;
        }
        return role.getName().toString();
    }
}

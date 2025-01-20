package com.marketplace.core.utils.mapper;

import com.marketplace.core.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    default String toDto(Role role) {
        if (role == null || role.getName() == null) {
            return null;
        }

        return role.getName().toString();
    }
}

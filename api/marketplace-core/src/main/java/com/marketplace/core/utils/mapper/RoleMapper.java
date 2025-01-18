package com.marketplace.core.utils.mapper;

import com.marketplace.core.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    default String toDto(Role role) {
        return role.getName().toString();
    }
}

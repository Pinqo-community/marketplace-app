package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.core.entity.User;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between User entities and DTOs.
 * Uses MapStruct for automatic mapping implementation with RoleMapper for role conversions.
 */
@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    /**
     * Converts a User entity to a DTO.
     * Uses RoleMapper for converting role information.
     *
     * @param user the user entity to convert
     * @return the converted user DTO
     */
    UserDTO toDto(User user);
}

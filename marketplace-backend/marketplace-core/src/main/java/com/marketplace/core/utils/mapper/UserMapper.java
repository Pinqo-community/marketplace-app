package com.marketplace.core.utils.mapper;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.core.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    UserDTO toDto(User category);
}

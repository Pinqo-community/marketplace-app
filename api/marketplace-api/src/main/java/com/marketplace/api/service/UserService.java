package com.marketplace.api.service;

import com.marketplace.api.dto.UserDTO;
import com.marketplace.api.dto.UserInfos;

import java.util.Optional;

public interface UserService {
    UserDTO createUser(UserInfos userInfos);

    UserDTO getUserByEmail(String email);

    Optional<UserDTO> findByEmail(String email);

    UserDTO getUserById(Long id);
}

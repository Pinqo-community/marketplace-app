package com.marketplace.api.service;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.dto.user.UserInfos;

import java.util.Optional;

/**
 * Service interface for managing user operations.
 */
public interface UserService {
    /**
     * Creates a new user.
     *
     * @param userInfos user information for creation
     * @return UserDTO of created user
     */
    UserDTO createUser(UserInfos userInfos);

    /**
     * Retrieves a user by email.
     *
     * @param email user's email address
     * @return UserDTO of found user
     */
    UserDTO getUserByEmail(String email);

    /**
     * Searches for a user by email.
     *
     * @param email user's email address
     * @return Optional<UserDTO> containing user if found
     */
    Optional<UserDTO> findByEmail(String email);

    /**
     * Retrieves a user by ID.
     *
     * @param id unique identifier of the user
     * @return UserDTO of found user
     */
    UserDTO getUserById(Long id);
}

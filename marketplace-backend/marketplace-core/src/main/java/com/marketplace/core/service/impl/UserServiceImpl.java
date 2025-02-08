package com.marketplace.core.service.impl;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.dto.user.UserInfos;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.api.service.UserService;
import com.marketplace.core.entity.Buyer;
import com.marketplace.core.entity.Role;
import com.marketplace.core.entity.User;
import com.marketplace.core.entity.enums.RoleType;
import com.marketplace.core.repository.RoleRepository;
import com.marketplace.core.repository.UserRepository;
import com.marketplace.core.service.BuyerService;
import com.marketplace.core.utils.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of UserService interface for managing user operations.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BuyerService buyerService;
    private final UserMapper userMapper;

    /**
     * Creates a new user with associated buyer profile.
     *
     * @param userInfos user creation data
     * @return created user DTO
     * @throws NotFoundException if email already exists
     * @throws RuntimeException if role assignment fails
     */
    @Override
    public UserDTO createUser(UserInfos userInfos) {
        log.atDebug().log("Enter createUser(userInfos: {})", userInfos);

        if (userRepository.existsByEmail(userInfos.getEmail())) {
            log.atError().log("Email already exists");
            throw new NotFoundException("Cet email est déjà utilisé");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Un problème est survenu dans la création de l'utilisateur")));

        Buyer buyer = buyerService.createBuyer(userInfos.getFirstName(), userInfos.getLastName());

        User newUser = User.builder()
                .email(userInfos.getEmail())
                .password(userInfos.getPassword() != null ? userInfos.getPassword() : null)
                .provider(userInfos.getProvider())
                .enabled(true)
                .buyer(buyer)
                .roles(roles)
                .build();

        UserDTO userDto = userMapper.toDto(userRepository.save(newUser));

        log.atDebug().log("Leave createUser() - return {}", userDto);

        return userDto;
    }

    /**
     * Finds user by email.
     *
     * @param email user email
     * @return optional containing user DTO if found
     */
    @Override
    public Optional<UserDTO> findByEmail(String email) {
        log.atDebug().log("Enter findByEmail(email: {})", email);
        Optional<UserDTO> result = userRepository.findByEmail(email).map(userMapper::toDto);
        log.atDebug().log("Leave findByEmail() - return {}", result);
        return result;
    }

    /**
     * Retrieves user by email.
     *
     * @param email user email
     * @return user DTO
     * @throws NotFoundException if user not found
     */
    @Override
    public UserDTO getUserByEmail(String email) {
        log.atDebug().log("Enter getUserByEmail(email: {})", email);

        UserDTO userDto = userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found"));

        log.atDebug().log("Leave getUserByEmail() - return {}", userDto);

        return userDto;
    }

    /**
     * Retrieves user by ID.
     *
     * @param id user identifier
     * @return user DTO
     * @throws NotFoundException if user not found
     */
    @Override
    public UserDTO getUserById(Long id) {
        log.atDebug().log("Enter getUserById(id: {})", id);

        UserDTO userDto = userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found"));

        log.atDebug().log("Leave getUserById() - return {}", userDto);

        return userDto;
    }
}

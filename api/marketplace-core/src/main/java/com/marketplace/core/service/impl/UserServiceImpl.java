package com.marketplace.core.service.impl;

import com.marketplace.api.dto.UserDTO;
import com.marketplace.api.dto.UserInfos;
import com.marketplace.api.service.UserService;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.core.utils.mapper.UserMapper;
import com.marketplace.core.entity.Buyer;
import com.marketplace.core.entity.Role;
import com.marketplace.core.entity.User;
import com.marketplace.core.entity.enums.RoleType;
import com.marketplace.core.repository.RoleRepository;
import com.marketplace.core.repository.UserRepository;
import com.marketplace.core.service.BuyerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BuyerService buyerService;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(UserInfos userInfos) {
        log.debug("Enter createUser(userInfos = {})", userInfos);

        if (userRepository.existsByEmail(userInfos.getEmail())) {
            log.error("Email already exists");
            throw new NotFoundException("Cet email est déjà utilisé");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleType.ROLE_USER).orElseThrow(() -> new RuntimeException("Un problème est survenu dans la création de l'utilisateur")));

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

        log.debug("Leave createUser() - return {}", userDto);

        return userDto;
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        log.debug("Enter getUserByEmail(email = {})", email);

        UserDTO userDto = userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found"));

        log.debug("Leave getUserByEmail() - return {}", userDto);

        return userDto;
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.debug("Enter getUserById(id = {})", id);

        UserDTO userDto = userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found"));

        log.debug("Leave getUserById() - return {}", userDto);

        return userDto;
    }
}

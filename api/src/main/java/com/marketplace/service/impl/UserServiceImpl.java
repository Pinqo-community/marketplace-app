package com.marketplace.service.impl;

import com.marketplace.entity.Buyer;
import com.marketplace.entity.Role;
import com.marketplace.entity.User;
import com.marketplace.exception.UserAlreadyExistsException;
import com.marketplace.model.RoleType;
import com.marketplace.repository.RoleRepository;
import com.marketplace.repository.UserRepository;
import com.marketplace.model.user.UserInfos;
import com.marketplace.service.BuyerService;
import com.marketplace.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final BuyerService buyerService;

    @Override
    public User createUser(UserInfos userInfos) {
        if (userRepository.existsByEmail(userInfos.getEmail())) {
            throw new UserAlreadyExistsException("Cet email est déjà utilisé");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleType.ROLE_USER).orElseThrow(() -> new RuntimeException("Un problème est survenu dans la création de l'utilisateur")));

        Buyer buyer = buyerService.createBuyer(userInfos.getFirstName(), userInfos.getLastName());

        User newUser = User.builder()
                .email(userInfos.getEmail())
                .password(userInfos.getPassword() != null ? passwordEncoder.encode(userInfos.getPassword()) : null)
                .provider(userInfos.getProvider())
                .enabled(true)
                .buyer(buyer)
                .roles(roles)
                .build();

        return userRepository.save(newUser);
    }
}

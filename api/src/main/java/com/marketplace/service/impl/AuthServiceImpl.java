package com.marketplace.service.impl;

import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.Buyer;
import com.marketplace.entity.Role;
import com.marketplace.entity.User;
import com.marketplace.exception.UserAlreadyExistsException;
import com.marketplace.model.RoleType;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.RoleRepository;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final BuyerRepository buyerRepository;

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Cet email est déjà utilisé");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleType.ROLE_USER).orElseThrow(() -> new RuntimeException("Un problème est survenu lors de l'inscription")));

        Buyer buyer = createBuyer(request.firstname(), request.lastname());

        return userRepository.save(
                User.builder()
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .provider("local")
                        .roles(roles)
                        .buyer(buyer)
                        .enabled(true)
                        .build()
        );
    }

    private Buyer createBuyer(String firstName, String lastName) {
        return buyerRepository.save(
                Buyer.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .build()
        );
    }
}

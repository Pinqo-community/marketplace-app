package com.marketplace.service.impl;

import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.Buyer;
import com.marketplace.entity.Role;
import com.marketplace.entity.User;
import com.marketplace.exception.UserAlreadyExistsException;
import com.marketplace.model.RoleType;
import com.marketplace.model.user.BasicUserInfos;
import com.marketplace.model.user.UserInfos;
import com.marketplace.repository.RoleRepository;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.BuyerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BuyerService buyerService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserInfos userInfos;

    @BeforeEach
    void setUp() {
        userInfos = new BasicUserInfos(new RegisterRequest("test@example", "Password123!", "John", "Doe"));
    }

    @Nested
    class CreateUser {
        @Test
        void whenValidUser_thenReturnUser() {
            // Given
            Role role = mock(Role.class);
            Buyer buyer = mock(Buyer.class);
            User user = mock(User.class);

            when(userRepository.existsByEmail(anyString())).thenReturn(false);

            when(roleRepository.findByName(any(RoleType.class))).thenReturn(Optional.of(role));

            when(buyerService.createBuyer(anyString(), anyString())).thenReturn(buyer);

            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);

            // When
            User result = userService.createUser(userInfos);

            // Then
            assertNotNull(result);
            verify(userRepository).save(any(User.class));
        }

        @Test
        void whenEmailAlreadyExists_thenThrowException() {
            // Given
            when(userRepository.existsByEmail(anyString())).thenReturn(true);

            // When
            UserAlreadyExistsException result = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userInfos));

            // Then
            assertEquals("Cet email est déjà utilisé", result.getMessage());
        }

        @Test
        void whenRoleNotExists_thenThrowException() {
            // Given
            when(userRepository.existsByEmail(anyString())).thenReturn(false);

            when(roleRepository.findByName(any(RoleType.class))).thenThrow(new RuntimeException("Un problème est survenu dans la création de l'utilisateur"));

            // When
            RuntimeException result = assertThrows(RuntimeException.class, () -> userService.createUser(userInfos));

            // Then
            assertEquals("Un problème est survenu dans la création de l'utilisateur", result.getMessage());
        }
    }
}
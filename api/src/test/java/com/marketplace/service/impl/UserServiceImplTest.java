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
    private Role userRole;
    private Buyer buyer;

    @BeforeEach
    void setUp() {
        userInfos = new BasicUserInfos(new RegisterRequest("test@example", "Password123!", "John", "Doe"));

        userRole = new Role();
        userRole.setName(RoleType.ROLE_USER);

        buyer = new Buyer();
        buyer.setFirstName("John");
        buyer.setLastName("Doe");
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(RoleType.ROLE_USER))
                .thenReturn(Optional.of(userRole));
        when(buyerService.createBuyer(anyString(), anyString()))
                .thenReturn(buyer);
        when(passwordEncoder.encode(anyString()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(userInfos);

        assertNotNull(result);
        assertEquals(userInfos.getEmail(), result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(1, result.getRoles().size());
        assertTrue(result.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleType.ROLE_USER));
        assertTrue(result.isEnabled());
        assertEquals(buyer, result.getBuyer());

        verify(userRepository).existsByEmail(userInfos.getEmail());
        verify(roleRepository).findByName(RoleType.ROLE_USER);
        verify(buyerService).createBuyer(userInfos.getFirstName(), userInfos.getLastName());
        verify(passwordEncoder).encode(userInfos.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WithExistingEmail_ThrowsException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.createUser(userInfos));

        verify(userRepository).existsByEmail(userInfos.getEmail());
        verifyNoMoreInteractions(roleRepository, buyerService, passwordEncoder);
    }

    @Test
    void createUser_RoleNotFound_ThrowsException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(RoleType.ROLE_USER))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.createUser(userInfos));

        assertEquals("Un problème est survenu dans la création de l'utilisateur",
                exception.getMessage());

        verify(userRepository).existsByEmail(userInfos.getEmail());
        verify(roleRepository).findByName(RoleType.ROLE_USER);
        verifyNoMoreInteractions(buyerService, passwordEncoder);
    }
}
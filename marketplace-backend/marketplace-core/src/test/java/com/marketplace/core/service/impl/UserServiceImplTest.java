package com.marketplace.core.service.impl;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.dto.user.UserInfos;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.core.entity.Buyer;
import com.marketplace.core.entity.Role;
import com.marketplace.core.entity.User;
import com.marketplace.core.entity.enums.RoleType;
import com.marketplace.core.repository.RoleRepository;
import com.marketplace.core.repository.UserRepository;
import com.marketplace.core.service.BuyerService;
import com.marketplace.core.utils.mapper.UserMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private Role createRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleType.ROLE_USER);
        return role;
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .password("encodedPassword")
                .enabled(true)
                .provider("local")
                .roles(Set.of(createRole()))
                .buyer(Buyer.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build();
    }

    private UserDTO createUserDTO() {
        return new UserDTO(
                1L,
                "test@test.com",
                "encodedPassword",
                true,
                Set.of("ROLE_USER"),
                "local"
        );
    }

    @Nested
    class CreateUser {
        @Test
        void whenValidInfos_thenCreateUser() {
            // Given
            UserInfos userInfos = mock(UserInfos.class);
            when(userInfos.getEmail()).thenReturn("test@test.com");
            when(userInfos.getPassword()).thenReturn("password");
            when(userInfos.getFirstName()).thenReturn("John");
            when(userInfos.getLastName()).thenReturn("Doe");
            when(userInfos.getProvider()).thenReturn("local");

            Role role = createRole();
            User user = createUser();
            UserDTO expectedDto = createUserDTO();
            Buyer buyer = user.getBuyer();

            when(userRepository.existsByEmail(userInfos.getEmail())).thenReturn(false);
            when(roleRepository.findByName(RoleType.ROLE_USER)).thenReturn(Optional.of(role));
            when(buyerService.createBuyer(userInfos.getFirstName(), userInfos.getLastName())).thenReturn(buyer);
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toDto(user)).thenReturn(expectedDto);

            // When
            UserDTO result = userService.createUser(userInfos);

            // Then
            assertThat(result).isEqualTo(expectedDto);
            verify(userRepository).existsByEmail(userInfos.getEmail());
            verify(roleRepository).findByName(RoleType.ROLE_USER);
            verify(buyerService).createBuyer(userInfos.getFirstName(), userInfos.getLastName());
            verify(userRepository).save(argThat(savedUser ->
                    savedUser.getEmail().equals(userInfos.getEmail()) &&
                            savedUser.getPassword().equals(userInfos.getPassword()) &&
                            savedUser.getProvider().equals(userInfos.getProvider())
            ));
            verify(userMapper).toDto(user);
        }

        @Test
        void whenEmailExists_thenThrowException() {
            // Given
            UserInfos userInfos = mock(UserInfos.class);
            when(userInfos.getEmail()).thenReturn("test@test.com");

            when(userRepository.existsByEmail(userInfos.getEmail())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> userService.createUser(userInfos))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Cet email est déjà utilisé");

            verify(userRepository).existsByEmail(userInfos.getEmail());
            verifyNoMoreInteractions(roleRepository, buyerService, userRepository, userMapper);
        }

        @Test
        void whenRoleNotFound_thenThrowException() {
            // Given
            UserInfos userInfos = mock(UserInfos.class);
            when(userInfos.getEmail()).thenReturn("test@test.com");

            when(userRepository.existsByEmail(userInfos.getEmail())).thenReturn(false);
            when(roleRepository.findByName(RoleType.ROLE_USER)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.createUser(userInfos))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Un problème est survenu dans la création de l'utilisateur");

            verify(userRepository).existsByEmail(userInfos.getEmail());
            verify(roleRepository).findByName(RoleType.ROLE_USER);
            verifyNoMoreInteractions(buyerService, userRepository, userMapper);
        }

        @Test
        void whenPasswordIsNull_thenCreateUserWithNullPassword() {
            // Given
            UserInfos userInfos = mock(UserInfos.class);
            when(userInfos.getEmail()).thenReturn("test@test.com");
            when(userInfos.getPassword()).thenReturn(null);
            when(userInfos.getFirstName()).thenReturn("John");
            when(userInfos.getLastName()).thenReturn("Doe");
            when(userInfos.getProvider()).thenReturn("google");

            Role role = createRole();
            User user = createUser();
            UserDTO expectedDto = createUserDTO();
            Buyer buyer = user.getBuyer();

            when(userRepository.existsByEmail(userInfos.getEmail())).thenReturn(false);
            when(roleRepository.findByName(RoleType.ROLE_USER)).thenReturn(Optional.of(role));
            when(buyerService.createBuyer(userInfos.getFirstName(), userInfos.getLastName())).thenReturn(buyer);
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toDto(user)).thenReturn(expectedDto);

            // When
            UserDTO result = userService.createUser(userInfos);

            // Then
            assertThat(result).isEqualTo(expectedDto);
            verify(userRepository).save(argThat(savedUser -> savedUser.getPassword() == null));
        }
    }

    @Nested
    class FindByEmail {
        @Test
        void whenEmailExists_thenReturnUser() {
            // Given
            String email = "test@test.com";
            User user = createUser();
            UserDTO expectedDto = createUserDTO();

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(expectedDto);

            // When
            Optional<UserDTO> result = userService.findByEmail(email);

            // Then
            assertThat(result)
                    .isPresent()
                    .contains(expectedDto);
            verify(userRepository).findByEmail(email);
            verify(userMapper).toDto(user);
        }

        @Test
        void whenEmailNotExists_thenReturnEmpty() {
            // Given
            String email = "nonexistent@test.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            // When
            Optional<UserDTO> result = userService.findByEmail(email);

            // Then
            assertThat(result).isEmpty();
            verify(userRepository).findByEmail(email);
            verifyNoInteractions(userMapper);
        }
    }

    @Nested
    class GetUserByEmail {
        @Test
        void whenEmailExists_thenReturnUser() {
            // Given
            String email = "test@test.com";
            User user = createUser();
            UserDTO expectedDto = createUserDTO();

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(expectedDto);

            // When
            UserDTO result = userService.getUserByEmail(email);

            // Then
            assertThat(result).isEqualTo(expectedDto);
            verify(userRepository).findByEmail(email);
            verify(userMapper).toDto(user);
        }

        @Test
        void whenEmailNotExists_thenThrowException() {
            // Given
            String email = "nonexistent@test.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.getUserByEmail(email))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("User not found");

            verify(userRepository).findByEmail(email);
            verifyNoInteractions(userMapper);
        }
    }

    @Nested
    class GetUserById {
        @Test
        void whenIdExists_thenReturnUser() {
            // Given
            Long id = 1L;
            User user = createUser();
            UserDTO expectedDto = createUserDTO();

            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(expectedDto);

            // When
            UserDTO result = userService.getUserById(id);

            // Then
            assertThat(result).isEqualTo(expectedDto);
            verify(userRepository).findById(id);
            verify(userMapper).toDto(user);
        }

        @Test
        void whenIdNotExists_thenThrowException() {
            // Given
            Long id = 999L;
            when(userRepository.findById(id)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.getUserById(id))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("User not found");

            verify(userRepository).findById(id);
            verifyNoInteractions(userMapper);
        }
    }
}
package com.marketplace.auth.service.impl;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.api.service.UserService;
import com.marketplace.auth.security.SecurityUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void whenValidEmail_thenUserShouldBeFound() {
        // Given
        String email = "test@test.com";
        UserDTO userDTO = new UserDTO(
                1L,
                email,
                "encodedPassword",
                true,
                Set.of("ROLE_USER"),
                "local"
        );

        when(userService.getUserByEmail(email)).thenReturn(userDTO);

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo(userDTO.password());
        assertThat(userDetails.isEnabled()).isEqualTo(userDTO.enabled());
        assertThat(userDetails)
                .isInstanceOf(SecurityUser.class)
                .extracting("user")
                .isEqualTo(userDTO);
    }

    @Test
    void whenInvalidEmail_thenThrowUsernameNotFoundException() {
        // Given
        String email = "nonexistent@test.com";
        when(userService.getUserByEmail(email))
                .thenThrow(new NotFoundException("Utilisateur non trouvé"));

        // When & Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(email))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Utilisateur non trouvé");
    }
}
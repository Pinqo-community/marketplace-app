package com.marketplace.auth.security;

import com.marketplace.api.dto.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityUserTest {

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password123";

    private UserDTO userDTO;
    private SecurityUser securityUser;

    @BeforeEach
    void setUp() {
        userDTO = mock(UserDTO.class);
        when(userDTO.email()).thenReturn(EMAIL);
        when(userDTO.password()).thenReturn(PASSWORD);
        when(userDTO.enabled()).thenReturn(true);
        when(userDTO.roles()).thenReturn(Set.of("ROLE_USER", "ROLE_ADMIN"));

        securityUser = new SecurityUser(userDTO);
    }

    @Test
    void testGetUsername() {
        // When
        String username = securityUser.getUsername();

        // Then
        assertEquals(EMAIL, username);
        verify(userDTO).email();
    }

    @Test
    void testGetPassword() {
        // When
        String password = securityUser.getPassword();

        // Then
        assertEquals(PASSWORD, password);
        verify(userDTO).password();
    }

    @Test
    void testIsEnabled_WhenUserIsEnabled() {
        // When
        boolean enabled = securityUser.isEnabled();

        // Then
        assertTrue(enabled);
        verify(userDTO).enabled();
    }

    @Test
    void testIsEnabled_WhenUserIsDisabled() {
        // Given
        when(userDTO.enabled()).thenReturn(false);

        // When
        boolean enabled = securityUser.isEnabled();

        // Then
        assertFalse(enabled);
        verify(userDTO).enabled();
    }

    @Test
    void testGetAuthorities() {
        // When
        Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();

        // Then
        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        verify(userDTO).roles();
    }

    @Test
    void testGetAuthorities_WithEmptyRoles() {
        // Given
        when(userDTO.roles()).thenReturn(Set.of());

        // When
        Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();

        // Then
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
        verify(userDTO).roles();
    }

    @Test
    void testGetAuthorities_WithNullRoles() {
        // Given
        when(userDTO.roles()).thenReturn(null);

        // When/Then
        assertThrows(NullPointerException.class, () -> securityUser.getAuthorities());
        verify(userDTO).roles();
    }

    @Test
    void testAccountStatuses() {
        // When/Then
        assertTrue(securityUser.isAccountNonExpired(), "Account should be non-expired by default");
        assertTrue(securityUser.isAccountNonLocked(), "Account should be non-locked by default");
        assertTrue(securityUser.isCredentialsNonExpired(), "Credentials should be non-expired by default");
    }
}
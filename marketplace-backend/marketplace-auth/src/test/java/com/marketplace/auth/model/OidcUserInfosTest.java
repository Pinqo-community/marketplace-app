package com.marketplace.auth.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OidcUserInfosTest {

    @Mock
    private OidcUser oidcUser;

    private static final String PROVIDER = "google";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";

    private OidcUserInfos userInfos;

    @BeforeEach
    void setUp() {
        userInfos = new OidcUserInfos(oidcUser, PROVIDER);
    }

    @Test
    void testGetEmail() {
        // Given
        when(oidcUser.getEmail()).thenReturn(TEST_EMAIL);

        // When
        String email = userInfos.getEmail();

        // Then
        assertEquals(TEST_EMAIL, email);
        verify(oidcUser).getEmail();
    }

    @Test
    void testGetEmailWithNull() {
        // Given
        when(oidcUser.getEmail()).thenReturn(null);

        // When
        String email = userInfos.getEmail();

        // Then
        assertNull(email);
        verify(oidcUser).getEmail();
    }

    @Test
    void testGetPassword_AlwaysReturnsNull() {
        // When
        String password = userInfos.getPassword();

        // Then
        assertNull(password);
        verifyNoInteractions(oidcUser);
    }

    @Test
    void testGetFirstName() {
        // Given
        when(oidcUser.getGivenName()).thenReturn(FIRST_NAME);

        // When
        String firstName = userInfos.getFirstName();

        // Then
        assertEquals(FIRST_NAME, firstName);
        verify(oidcUser).getGivenName();
    }

    @Test
    void testGetFirstNameWithNull() {
        // Given
        when(oidcUser.getGivenName()).thenReturn(null);

        // When
        String firstName = userInfos.getFirstName();

        // Then
        assertNull(firstName);
        verify(oidcUser).getGivenName();
    }

    @Test
    void testGetLastName() {
        // Given
        when(oidcUser.getFamilyName()).thenReturn(LAST_NAME);

        // When
        String lastName = userInfos.getLastName();

        // Then
        assertEquals(LAST_NAME, lastName);
        verify(oidcUser).getFamilyName();
    }

    @Test
    void testGetLastNameWithNull() {
        // Given
        when(oidcUser.getFamilyName()).thenReturn(null);

        // When
        String lastName = userInfos.getLastName();

        // Then
        assertNull(lastName);
        verify(oidcUser).getFamilyName();
    }

    @Test
    void testGetProvider() {
        // When
        String provider = userInfos.getProvider();

        // Then
        assertEquals(PROVIDER, provider);
        verifyNoInteractions(oidcUser);
    }

    @Test
    void testConstructorWithNullProvider() {
        // When
        OidcUserInfos userInfosWithNullProvider = new OidcUserInfos(oidcUser, null);

        // Then
        assertNull(userInfosWithNullProvider.getProvider());
    }

    @Test
    void testAllMethodsWithEmptyValues() {
        // Given
        when(oidcUser.getEmail()).thenReturn("");
        when(oidcUser.getGivenName()).thenReturn("");
        when(oidcUser.getFamilyName()).thenReturn("");

        // When
        String email = userInfos.getEmail();
        String firstName = userInfos.getFirstName();
        String lastName = userInfos.getLastName();

        // Then
        assertEquals("", email);
        assertEquals("", firstName);
        assertEquals("", lastName);
        verify(oidcUser).getEmail();
        verify(oidcUser).getGivenName();
        verify(oidcUser).getFamilyName();
    }
}
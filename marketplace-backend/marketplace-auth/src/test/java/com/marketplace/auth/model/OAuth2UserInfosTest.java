package com.marketplace.auth.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2UserInfosTest {

    @Mock
    private OAuth2User oauth2User;

    private static final String PROVIDER = "google";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String FULL_NAME = "John Doe";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";

    private OAuth2UserInfos userInfos;

    @BeforeEach
    void setUp() {
        userInfos = new OAuth2UserInfos(oauth2User, PROVIDER);
    }

    @Test
    void testGetEmail() {
        // Given
        when(oauth2User.getAttribute("email")).thenReturn(TEST_EMAIL);

        // When
        String email = userInfos.getEmail();

        // Then
        assertEquals(TEST_EMAIL, email);
        verify(oauth2User).getAttribute("email");
    }

    @Test
    void testGetPassword_AlwaysReturnsNull() {
        // When
        String password = userInfos.getPassword();

        // Then
        assertNull(password);
        verifyNoInteractions(oauth2User);
    }

    @Test
    void testGetFirstName_WithFullName() {
        // Given
        when(oauth2User.getAttribute("name")).thenReturn(FULL_NAME);

        // When
        String firstName = userInfos.getFirstName();

        // Then
        assertEquals(FIRST_NAME, firstName);
        verify(oauth2User).getAttribute("name");
    }

    @Test
    void testGetLastName_WithFullName() {
        // Given
        when(oauth2User.getAttribute("name")).thenReturn(FULL_NAME);

        // When
        String lastName = userInfos.getLastName();

        // Then
        assertEquals(LAST_NAME, lastName);
        verify(oauth2User).getAttribute("name");
    }

    @Test
    void testGetFirstName_WithSingleName() {
        // Given
        when(oauth2User.getAttribute("name")).thenReturn("John");

        // When
        String firstName = userInfos.getFirstName();
        String lastName = userInfos.getLastName();

        // Then
        assertEquals("John", firstName);
        assertEquals("", lastName);
        verify(oauth2User, times(2)).getAttribute("name");
    }

    @Test
    void testGetName_WithNullName() {
        // Given
        when(oauth2User.getAttribute("name")).thenReturn(null);

        // When
        String firstName = userInfos.getFirstName();
        String lastName = userInfos.getLastName();

        // Then
        assertEquals("", firstName);
        assertEquals("", lastName);
        verify(oauth2User, times(2)).getAttribute("name");
    }

    @Test
    void testGetName_WithMultipleNames() {
        // Given
        when(oauth2User.getAttribute("name")).thenReturn("John Middle Doe");

        // When
        String firstName = userInfos.getFirstName();
        String lastName = userInfos.getLastName();

        // Then
        assertEquals("John", firstName);
        assertEquals("Doe", lastName);
        verify(oauth2User, times(2)).getAttribute("name");
    }

    @Test
    void testGetName_WithEmptyString() {
        // Given
        when(oauth2User.getAttribute("name")).thenReturn("");

        // When
        String firstName = userInfos.getFirstName();
        String lastName = userInfos.getLastName();

        // Then
        assertEquals("", firstName);
        assertEquals("", lastName);
        verify(oauth2User, times(2)).getAttribute("name");
    }

    @Test
    void testGetProvider() {
        // When
        String provider = userInfos.getProvider();

        // Then
        assertEquals(PROVIDER, provider);
        verifyNoInteractions(oauth2User);
    }

    @Test
    void testConstructorWithNullProvider() {
        // When
        OAuth2UserInfos userInfosWithNullProvider = new OAuth2UserInfos(oauth2User, null);

        // Then
        assertNull(userInfosWithNullProvider.getProvider());
    }
}
package com.marketplace.auth.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class BasicUserInfosTest {

    private static final String SAMPLE_EMAIL = "test@example.com";
    private static final String SAMPLE_PASSWORD = "password123";
    private static final String SAMPLE_FIRSTNAME = "John";
    private static final String SAMPLE_LASTNAME = "Doe";

    @Test
    void testBasicUserInfosConstruction() {
        // When
        BasicUserInfos userInfos = new BasicUserInfos(
                SAMPLE_EMAIL,
                SAMPLE_PASSWORD,
                SAMPLE_FIRSTNAME,
                SAMPLE_LASTNAME
        );

        // Then
        assertNotNull(userInfos);
        assertEquals(SAMPLE_EMAIL, userInfos.getEmail());
        assertEquals(SAMPLE_PASSWORD, userInfos.getPassword());
        assertEquals(SAMPLE_FIRSTNAME, userInfos.getFirstName());
        assertEquals(SAMPLE_LASTNAME, userInfos.getLastName());
        assertEquals("local", userInfos.getProvider());
    }

    @ParameterizedTest
    @CsvSource({
            "user1@test.com, pass123, Alice, Smith",
            "user2@test.com, pass456, Bob, Johnson",
            "user3@test.com, pass789, Charlie, Brown"
    })
    void testBasicUserInfosWithDifferentValues(
            String email,
            String password,
            String firstName,
            String lastName
    ) {
        // When
        BasicUserInfos userInfos = new BasicUserInfos(email, password, firstName, lastName);

        // Then
        assertEquals(email, userInfos.getEmail());
        assertEquals(password, userInfos.getPassword());
        assertEquals(firstName, userInfos.getFirstName());
        assertEquals(lastName, userInfos.getLastName());
        assertEquals("local", userInfos.getProvider());
    }

    @Test
    void testNullValues() {
        // When/Then
        assertDoesNotThrow(() -> new BasicUserInfos(null, null, null, null));

        BasicUserInfos userInfos = new BasicUserInfos(null, null, null, null);

        assertNull(userInfos.getEmail());
        assertNull(userInfos.getPassword());
        assertNull(userInfos.getFirstName());
        assertNull(userInfos.getLastName());
        assertEquals("local", userInfos.getProvider());
    }

    @Test
    void testEmptyValues() {
        // When
        BasicUserInfos userInfos = new BasicUserInfos("", "", "", "");

        // Then
        assertEquals("", userInfos.getEmail());
        assertEquals("", userInfos.getPassword());
        assertEquals("", userInfos.getFirstName());
        assertEquals("", userInfos.getLastName());
        assertEquals("local", userInfos.getProvider());
    }
}
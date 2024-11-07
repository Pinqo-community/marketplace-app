package com.marketplace.utils.oauth;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.Serializable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CookieUtilsTest {
    @Test
    void getCookie_WhenCookieExists_ShouldReturnCookie() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie expectedCookie = new Cookie("test-cookie", "test-value");
        request.setCookies(expectedCookie);

        Optional<Cookie> result = CookieUtils.getCookie(request, "test-cookie");

        assertTrue(result.isPresent());
        assertEquals("test-cookie", result.get().getName());
        assertEquals("test-value", result.get().getValue());
    }

    @Test
    void getCookie_WhenCookieDoesNotExist_ShouldReturnEmpty() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("other-cookie", "other-value"));

        Optional<Cookie> result = CookieUtils.getCookie(request, "test-cookie");

        assertFalse(result.isPresent());
    }

    @Test
    void getCookie_WhenNoCookies_ShouldReturnEmpty() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Optional<Cookie> result = CookieUtils.getCookie(request, "test-cookie");

        assertFalse(result.isPresent());
    }

    @Test
    void addCookie_ShouldAddCookieWithCorrectAttributes() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        String name = "test-cookie";
        String value = "test-value";
        int maxAge = 3600;

        CookieUtils.addCookie(response, name, value, maxAge);

        Cookie[] cookies = response.getCookies();
        assertEquals(1, cookies.length);
        Cookie addedCookie = cookies[0];
        assertEquals(name, addedCookie.getName());
        assertEquals(value, addedCookie.getValue());
        assertEquals(maxAge, addedCookie.getMaxAge());
        assertEquals("/", addedCookie.getPath());
        assertTrue(addedCookie.isHttpOnly());
    }

    @Test
    void deleteCookie_WhenCookieExists_ShouldDeleteCookie() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Cookie cookieToDelete = new Cookie("test-cookie", "test-value");
        request.setCookies(cookieToDelete);

        CookieUtils.deleteCookie(request, response, "test-cookie");

        Cookie[] responseCookies = response.getCookies();
        assertEquals(1, responseCookies.length);
        Cookie deletedCookie = responseCookies[0];
        assertEquals("test-cookie", deletedCookie.getName());
        assertEquals("", deletedCookie.getValue());
        assertEquals(0, deletedCookie.getMaxAge());
        assertEquals("/", deletedCookie.getPath());
    }

    @Test
    void deleteCookie_WhenCookieDoesNotExist_ShouldDoNothing() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setCookies(new Cookie("other-cookie", "other-value"));

        CookieUtils.deleteCookie(request, response, "test-cookie");

        assertEquals(0, response.getCookies().length);
    }

    @Test
    void serialize_ShouldSerializeObjectToBase64String() {
        TestObject testObject = new TestObject("test value");

        String serialized = CookieUtils.serialize(testObject);

        assertNotNull(serialized);
        assertTrue(serialized.length() > 0);
        assertDoesNotThrow(() -> java.util.Base64.getUrlDecoder().decode(serialized));
    }

    @Test
    void deserialize_ShouldDeserializeFromCookie() {
        TestObject originalObject = new TestObject("test value");
        String serialized = CookieUtils.serialize(originalObject);
        Cookie cookie = new Cookie("test-cookie", serialized);

        TestObject deserialized = CookieUtils.deserialize(cookie, TestObject.class);

        assertNotNull(deserialized);
        assertEquals(originalObject.getValue(), deserialized.getValue());
    }

    @Test
    void deserialize_WithInvalidData_ShouldThrowException() {
        Cookie cookie = new Cookie("test-cookie", "invalid-base64-data");

        assertThrows(IllegalArgumentException.class, () ->
                CookieUtils.deserialize(cookie, TestObject.class));
    }

    // serialisation/deserialisation class for testing
    private static class TestObject implements Serializable {
        private final String value;

        public TestObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
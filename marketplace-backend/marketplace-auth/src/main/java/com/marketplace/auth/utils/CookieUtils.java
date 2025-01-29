package com.marketplace.auth.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

/**
 * Utility class providing methods for HTTP cookie management.
 * Handles cookie creation, retrieval, deletion, and serialization operations.
 */
public class CookieUtils {

    /**
     * Retrieves a specific cookie from the HTTP request.
     *
     * @param request HTTP request containing cookies
     * @param name name of the cookie to find
     * @return Optional containing the cookie if found, empty if not found
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Creates and adds a new cookie to the HTTP response.
     *
     * @param response HTTP response to add the cookie to
     * @param name name of the cookie
     * @param value value to store in the cookie
     * @param maxAge maximum age of the cookie in seconds
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * Deletes a cookie by setting its maximum age to zero.
     *
     * @param request HTTP request containing the cookie
     * @param response HTTP response to send the deletion instruction
     * @param name name of the cookie to delete
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * Serializes an object to a Base64 encoded string.
     *
     * @param object object to serialize
     * @return Base64 encoded string representation of the object
     */
    public static String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }

    /**
     * Deserializes a cookie's value to an object of specified type.
     *
     * @param cookie cookie containing the serialized data
     * @param cls class type to deserialize to
     * @return deserialized object of specified type
     * @param <T> type parameter for deserialization
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())
        ));
    }
}

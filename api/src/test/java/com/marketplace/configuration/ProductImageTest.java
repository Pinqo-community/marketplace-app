package com.marketplace.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for ProductImage")
@Nested
public class ProductImageTest {

    @Test
    @DisplayName("Test Fetch Multiple Images From Unsplash Collection")
    public void testFetchImagesFromUnsplashCollection() {
        String apiKey = System.getenv("UNSPLASH_API_KEY");
        assertNotNull(apiKey, "API Key should be set in the environment variables.");

        ProductDataInitializer initializer = new ProductDataInitializer(null, new RestTemplate());
        ReflectionTestUtils.setField(initializer, "apiKey", apiKey);

        List<String> images = initializer.fetchImagesFromUnsplashCollection("1155327", 10);

        // Assertions
        assertNotNull(images, "Image list should not be null.");
        assertFalse(images.isEmpty(), "Image list should contain images.");
        assertTrue(images.size() <= 10, "Image list size should not exceed the requested number.");
        assertFalse(images.contains("https://via.placeholder.com/300"), "Image list should not contain placeholder URLs.");
    }

    @Test
    @DisplayName("Test Fetch Image From Unsplash with Valid API Key")
    public void testFetchImageFromUnsplash_withValidApiKey() {
        String apiKey = System.getenv("UNSPLASH_API_KEY");
        assertNotNull(apiKey, "API Key should be set in the environment variables.");

        ProductDataInitializer initializer = new ProductDataInitializer(null, new RestTemplate());
        ReflectionTestUtils.setField(initializer, "apiKey", apiKey);

        try {

            List<String> images = initializer.fetchImagesFromUnsplashCollection("1155327", 5);

            assertNotNull(images, "Image list should not be null.");
            assertFalse(images.isEmpty(), "Image list should not be empty.");
            assertFalse(images.contains("https://via.placeholder.com/300"), "Image list should not contain placeholder URLs.");

        } catch (HttpClientErrorException e) {
            Assertions.fail("API call failed with status: " + e.getStatusCode() + " and message: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test Fetch Images From Unsplash Collection with Error")
    public void testFetchImagesFromUnsplashCollection_withError() {
        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        ProductDataInitializer initializer = new ProductDataInitializer(null, mockRestTemplate);
        ReflectionTestUtils.setField(initializer, "apiKey", "invalid_api_key");

        Mockito.when(mockRestTemplate.exchange(
                Mockito.anyString(),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(String.class)
        )).thenThrow(new RuntimeException("Simulated API error"));

        List<String> images = initializer.fetchImagesFromUnsplashCollection("1155327", 10);

       //Assertions
        assertNotNull(images, "Image list should not be null even in case of error.");
        assertEquals(List.of("https://via.placeholder.com/300"), images,
                "Image list should contain a placeholder in case of error.");
    }
}

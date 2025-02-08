package com.marketplace.core.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductDataInitializerTest {
    @Test
    public void testFetchImagesFromUnsplashWithMock() {
        // Mock du RestTemplate
        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);

        // Instance de ProductDataInitializer avec le RestTemplate mocké
        ProductDataInitializer initializer = new ProductDataInitializer(null, mockRestTemplate);

        // Mock de la réponse de l'API
        String mockResponse = """
        [
            {"urls": {"small": "https://image-url-1.com"}},
            {"urls": {"small": "https://image-url-2.com"}}
        ]
    """;

        Mockito.when(mockRestTemplate.exchange(
                Mockito.anyString(),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(String.class)
        )).thenReturn(new org.springframework.http.ResponseEntity<>(mockResponse, org.springframework.http.HttpStatus.OK));

        // Appel de la méthode testée
        List<String> images = initializer.fetchImagesFromUnsplashCollection("1165522", 2);

        // Assertions
        assertNotNull(images, "Image list should not be null.");
        assertEquals(2, images.size(), "Image list should contain two images.");
        assertEquals("https://image-url-1.com&w=300&h=300", images.get(0));
        assertEquals("https://image-url-2.com&w=300&h=300", images.get(1));
    }


    @Test
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
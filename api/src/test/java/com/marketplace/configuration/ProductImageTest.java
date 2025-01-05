package com.marketplace.configuration;

import com.marketplace.entity.Product;
import com.marketplace.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("Test Product Images with Mocked Data")
    public void testProductImages_withMockedData() {

        Product product1 = Product.builder().name("Apple").photo("https://valid-image-url.com").build();
        Product product2 = Product.builder().name("Orange").photo("https://valid-image-url.com").build();
        List<Product> mockProducts = List.of(product1, product2);

        Mockito.when(productRepository.findAll()).thenReturn(mockProducts);

        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            assertNotNull(product.getPhoto(), "Image URL is null for product: " + product.getName());
            assertFalse(product.getPhoto().equals("https://via.placeholder.com/300"),
                    "Placeholder image found for product: " + product.getName());
        }
    }

    @Test
    @DisplayName("Test Fetch Image From Pexels with Valid API Key")
    public void testFetchImageFromPexels_withValidApiKey() {
        String apiKey = System.getenv("PEXELS_API_KEY");
        assertNotNull(apiKey, "API Key should be set in the environment variables.");

        ProductDataInitializer initializer = new ProductDataInitializer(null, new RestTemplate());
        ReflectionTestUtils.setField(initializer, "apiKey", apiKey);

        try {

            String imageUrl = initializer.fetchImageFromPexels("apple");

            assertNotNull(imageUrl, "Image URL should not be null.");
            assertFalse(imageUrl.equals("https://via.placeholder.com/300"), "Placeholder image returned unexpectedly.");

        } catch (HttpClientErrorException e) {
            Assertions.fail("API call failed with status: " + e.getStatusCode() + " and message: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test Fetch Image From Pexels with Error Response")
    public void testFetchImageFromPexels_withErrorResponse() {
        String apiKey = System.getenv("PEXELS_API_KEY");
        assertNotNull(apiKey, "API Key should be set in the environment variables.");

        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        ProductDataInitializer initializer = new ProductDataInitializer(null, mockRestTemplate);
        ReflectionTestUtils.setField(initializer, "apiKey", apiKey);


        Mockito.when(mockRestTemplate.exchange(
                Mockito.anyString(),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(String.class)
        )).thenThrow(new RuntimeException("Simulated API error"));


        String imageUrl = initializer.fetchImageFromPexels("invalid_query");

        assertNotNull(imageUrl, "Image URL should not be null even in case of error.");
        assertEquals("https://via.placeholder.com/300", imageUrl, "Error case should return the placeholder URL.");
    }

}

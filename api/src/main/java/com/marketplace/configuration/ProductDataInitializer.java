package com.marketplace.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.entity.Product;
import com.marketplace.repository.ProductRepository;
import net.datafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.stream.IntStream;


/**
 * This class is responsible for fetching product images from Pexels API.
 * It is used for populating the product images in the database.
 */
@Component
@RequiredArgsConstructor
@Profile("dev")
public class ProductDataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

   @Value("${pexels.api.key}")
    private String apiKey;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {

            Faker faker = new Faker( Locale.ENGLISH);

            IntStream.range(0, 60).forEach(i -> {
                int stockQuantity = faker.number().numberBetween(1, 500);
                int maxQuantityByPurchase = faker.number().numberBetween(1, stockQuantity);
                int stepQuantity = faker.options().option(1, 2, 3, 5, 10);


                String productName = faker.commerce().productName();
                String imageUrl = fetchImageFromPexels(productName);

                Product product = Product.builder()
                        .name(productName)
                        .description(faker.lorem().sentence())
                        .photo(imageUrl)
                        .unitPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100)))
                        .nutritionalValue(String.join(", ", faker.food().spice()))
                        .listOfIngredients(String.join(", ", faker.food().ingredient()))
                        .stockQuantity(stockQuantity)
                        .maxQuantityByPurchase(maxQuantityByPurchase)
                        .stepQuantity(stepQuantity)
                        .criticalLevel(faker.number().numberBetween(1, 10))
                        .active(i % 5 != 0)
                        .build();
                productRepository.save(product);
                System.out.println("Saving product with image URL: " + imageUrl);

            });
            System.out.println("60 fake products have been added to the database.");


        }
    }

    /**
     * Fetches an image URL from the Pexels API based on the given query.
     *
     * @param query The search query (e.g., product name) to find a relevant image.
     * @return The URL of the image if found, or a default placeholder URL if any error occurs.
     */
    protected String fetchImageFromPexels(String query) {
        String url = "https://api.pexels.com/v1/search?query=" + query + "&per_page=1";

        try {
            // Set up headers with the API key
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", apiKey);
            // Create an HTTP entity with the headers(API request)
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

            // Parse the JSON response using Jackson
            if (response != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response);

                // Navigate to the URL of the first image
                JsonNode photosNode = rootNode.path("photos");
                if (photosNode.isArray() && photosNode.size() > 0) {
                    JsonNode photoNode = photosNode.get(0).path("src").path("original");
                    if (!photoNode.isMissingNode()) {
                        return photoNode.asText();
                    }
                }
            }
            // Return a placeholder image if the response is invalid
            return "https://via.placeholder.com/300";
        } catch (Exception e) {
            return "https://via.placeholder.com/300";
        }
    }
}











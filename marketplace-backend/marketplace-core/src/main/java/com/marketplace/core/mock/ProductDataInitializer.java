package com.marketplace.core.mock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.core.entity.Product;
import com.marketplace.core.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Profile("dev")
@Slf4j
public class ProductDataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    @Value("${unsplash.api.key}")
    private String apiKey;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            Faker faker = new Faker( Locale.FRANCE);

            List<String> localProductNames = Arrays.asList(
                    "Miel d'acacia", "Fromage de chèvre", "Pomme bio", "Pain complet",
                    "Confiture de framboises", "Herbes aromatiques fraîches", "Carottes du potager", "Poulet fermier",
                    "Huile d'olive artisanale", "Sirop d'érable biologique", "Yaourt fermier au lait entier",
                    "Tisanes aux plantes médicinales"," Graines de sésame ",
                    "Charcuterie artisanale fumée","Œufs de poules élevées en plein air",
                    "Beurre fermier au lait cru", "Fraises","Ratatouille aux légumes bio",
                    "Courgettes","Jus de pommes artisanal", "Huile de noix extra vierge",
                    "Cassoulet", "Pomme de Terre", "Tomate Bio",
                    "Laitue batavia"
            );

            List<String> imageUrls = fetchImagesFromUnsplashCollection("1155327", localProductNames.size());

            IntStream.range(0, 60).forEach(i -> {
                int stockQuantity = faker.number().numberBetween(1, 500);
                int maxQuantityByPurchase = faker.number().numberBetween(1, stockQuantity);
                int stepQuantity = faker.options().option(1, 2, 3, 5, 10);

                String imageUrl = imageUrls.get(i % imageUrls.size());
                String productName = localProductNames.get(faker.random().nextInt(localProductNames.size()));

                String listOfIngredients = String.join(", ", IntStream.range(0, 3)
                        .mapToObj(j -> faker.food().ingredient())
                        .distinct()
                        .toList());

                Product product = Product.builder()
                        .name(productName)
                        .description(faker.lorem().sentence())
                        .photo(imageUrl)
                        .unitPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100)))
                        .nutritionalValue(String.join(", ", faker.food().spice()))
                        .listOfIngredients(listOfIngredients)
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
     * @param collectionId Collection ID from Unsplash
     * @param totalImages Number of images to fetch
     * @return List of image URLs
     */
    protected List<String> fetchImagesFromUnsplashCollection(String collectionId, int totalImages) {
        String url = "https://api.unsplash.com/collections/" + collectionId + "/photos?per_page=" + totalImages;
        try {
            /* Set up headers with the API key */
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Client-ID " + apiKey);

            /* Create an HTTP entity with the headers(API request) */
            HttpEntity<String> entity = new HttpEntity<>(headers);
            System.out.println("Querying Unsplash with URL: " + url);

            /* Fetch the response */
            String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
            System.out.println("Response from Unsplash: " + response);

            /* Parse the JSON response using Jackson */
            if (response != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response);

                List<String> imageUrls = IntStream.range(0, rootNode.size())
                        .mapToObj(i -> {
                            String rawUrl = rootNode.get(i).path("urls").path("small").asText("https://via.placeholder.com/300");
                            return rawUrl + "&w=300&h=300";
                        })
                        .toList();

                System.out.println("Fetched " + imageUrls.size() + " images from Unsplash.");
                return imageUrls;
            }
        } catch (Exception e) {
            log.atError().log("An error occurred while fetching images from Unsplash: {}", e.getMessage());
            return List.of("https://via.placeholder.com/300");
        }

        /* Return an empty list if the request fails */
        return List.of();
    }
}

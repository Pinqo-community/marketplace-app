package com.marketplace.web.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS) settings.
 * Configures allowed origins, methods, and headers for cross-origin requests.
 */
@Configuration
public class CorsConfig {
    @Value("${app.client.allowed-urls}")
    private String clientUrl;

    /**
     * Creates a CORS configuration bean.
     * Configures CORS settings for the application including:
     * - Allowed origins from application properties
     * - Standard HTTP methods (GET, POST, PUT, DELETE)
     * - Common headers for API communication
     *
     * @return WebMvcConfigurer with CORS configuration
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(clientUrl.split(","))
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization");
            }
        };
    }
}

package com.marketplace.auth.configuration;

import com.marketplace.auth.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OAuth2 authentication.
 */
@Configuration
public class OAuth2Config {

    /**
     * Creates repository for storing OAuth2 authorization requests in cookies.
     *
     * @return cookie-based OAuth2 authorization request repository
     */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
}

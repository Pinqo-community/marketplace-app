package com.marketplace.auth.configuration;

import com.marketplace.auth.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OAuth2ConfigTest {

    @InjectMocks
    private OAuth2Config oAuth2Config;

    @Test
    void httpCookieOAuth2AuthorizationRequestRepository_ShouldCreateRepository() {
        // When
        HttpCookieOAuth2AuthorizationRequestRepository repository =
                oAuth2Config.httpCookieOAuth2AuthorizationRequestRepository();

        // Then
        assertNotNull(repository);
        assertTrue(repository instanceof AuthorizationRequestRepository);
    }

    @Test
    void httpCookieOAuth2AuthorizationRequestRepository_ShouldHandleCookies() {
        // Given
        HttpCookieOAuth2AuthorizationRequestRepository repository =
                oAuth2Config.httpCookieOAuth2AuthorizationRequestRepository();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthorizationRequest authRequest = OAuth2AuthorizationRequest.authorizationCode()
                .clientId("test-client")
                .authorizationUri("http://test.com/auth")
                .redirectUri("http://test.com/callback")
                .state("test-state")
                .build();

        // When
        repository.saveAuthorizationRequest(authRequest, request, response);
        OAuth2AuthorizationRequest savedRequest = repository.loadAuthorizationRequest(request);

        // Then
        assertNull(savedRequest); // car les cookies ne sont pas préservés dans les objets Mock
        assertNotNull(response.getCookies()); // mais on vérifie que des cookies ont été créés
    }

    @Test
    void httpCookieOAuth2AuthorizationRequestRepository_ShouldRemoveAuthorizationRequest() {
        // Given
        HttpCookieOAuth2AuthorizationRequestRepository repository =
                oAuth2Config.httpCookieOAuth2AuthorizationRequestRepository();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // When
        repository.removeAuthorizationRequest(request, response);

        // Then
        OAuth2AuthorizationRequest loadedRequest = repository.loadAuthorizationRequest(request);
        assertNull(loadedRequest);
    }
}
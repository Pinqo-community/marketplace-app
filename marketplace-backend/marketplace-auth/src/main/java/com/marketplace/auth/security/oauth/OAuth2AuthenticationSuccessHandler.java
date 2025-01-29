package com.marketplace.auth.security.oauth;

import com.marketplace.api.dto.auth.JwtResponse;
import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.service.UserService;
import com.marketplace.auth.service.JwtService;
import com.marketplace.auth.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Handler for successful OAuth2 authentication.
 * Manages redirection and token generation after successful authentication.
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final UserService userService;

    @Value("${app.client.allowed-urls}")
    private String clientUrl;

    /**
     * Handles successful authentication by determining target URL and redirecting.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param authentication authentication object containing principal
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * Determines the target URL for redirection after successful authentication.
     * Generates JWT tokens and adds them as query parameters.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param authentication authentication object containing principal
     * @return target URL with tokens
     * @throws RuntimeException if redirect URI is unauthorized or user cannot be accessed
     */
    @Override
    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request,
                        HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RuntimeException(
                    "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElseThrow();
        UserDTO user = getUserFromAuthentication(authentication);
        JwtResponse token = jwtService.generateJwtToken(user);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", token.accessToken())
                .queryParam("refresh_token", token.refreshToken())
                .build()
                .toUriString();
    }

    /**
     * Clears authentication attributes and cookies.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    /**
     * Validates if the redirect URI is authorized.
     *
     * @param uri redirect URI to validate
     * @return true if URI is authorized, false otherwise
     */
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        List<String> authorizedRedirectUris = Arrays.stream(clientUrl.split(","))
                .map(String::trim)
                .map(url -> url + "/oauth/redirect")
                .toList();

        return authorizedRedirectUris.stream()
                .map(URI::create)
                .anyMatch(authorizedURI ->
                        authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                                && authorizedURI.getPort() == clientRedirectUri.getPort());
    }

    /**
     * Extracts user information from authentication object.
     *
     * @param authentication authentication object containing principal
     * @return user DTO
     * @throws RuntimeException if user cannot be accessed
     */
    private UserDTO getUserFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof OidcUser) {
            return userService.getUserByEmail(((OidcUser) principal).getEmail());
        } else if (principal instanceof OAuth2User) {
            return userService.getUserByEmail(((OAuth2User) principal)
                    .getAttributes().get("email").toString());
        }
        throw new RuntimeException("A problem has occurred while trying to access the user");
    }
}

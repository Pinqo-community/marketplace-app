package com.marketplace.security.oauth;

import com.marketplace.dto.JwtResponse;
import com.marketplace.entity.User;
import com.marketplace.exception.UserNotFoundException;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.JwtService;
import com.marketplace.utils.oauth.CookieUtils;
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
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final UserRepository userRepository;

    @Value("${app.client.allowed-urls}")
    private String clientUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RuntimeException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElseThrow();

        Object principal = authentication.getPrincipal();
        User user = null;
        if (principal instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) principal;
            user = userRepository.findByEmail(oidcUser.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        } else if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            user = userRepository.findByEmail(oAuth2User.getAttributes().get("email").toString()).orElseThrow(() -> new UserNotFoundException("User not found"));
        }

        if (user == null) {
            throw new RuntimeException("A problem has occured while trying to access the user");
        }

        JwtResponse token = jwtService.generateJwtToken(user);

        userRepository.save(user);

        return UriComponentsBuilder.fromUriString(targetUrl).queryParam("access_token", token.accessToken()).queryParam("refresh_token", token.refreshToken()).build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // TODO: Replace redirect uri by the client one
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        String[] urls = clientUrl.split(",");
        List<String> authorizedRedirectUris = new ArrayList<>();
        for (String url : urls) {
            authorizedRedirectUris.add(url.trim() + "/api/v1/auth/oauth");
        }

        return authorizedRedirectUris.stream().anyMatch(authorizedRedirectUri -> {
            URI authorizedURI = URI.create(authorizedRedirectUri);
            return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) && authorizedURI.getPort() == clientRedirectUri.getPort();
        });
    }
}

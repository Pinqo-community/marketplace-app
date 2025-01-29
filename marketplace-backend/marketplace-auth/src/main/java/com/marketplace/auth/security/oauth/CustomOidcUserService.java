package com.marketplace.auth.security.oauth;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.exception.AlreadyExistsException;
import com.marketplace.api.service.UserService;
import com.marketplace.auth.model.OidcUserInfos;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom service for handling OIDC authentication process.
 * Extends OidcUserService to provide custom user loading logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOidcUserService extends OidcUserService {
    private final UserService userService;

    /**
     * Loads OIDC user details and creates or validates local user account.
     *
     * @param userRequest OIDC user request containing authentication details
     * @return authenticated OidcUser
     * @throws AlreadyExistsException if email exists with different provider
     */
    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        log.atDebug().log("Enter loadUser(userRequest: {})", userRequest);

        OidcUser oidcUser = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        Optional<UserDTO> user = userService.findByEmail(oidcUser.getEmail());

        if (user.isPresent() && !user.get().provider().equals(provider)) {
            log.atError().log("Account already exists with different provider");
            throw new AlreadyExistsException("Un compte existe déjà avec cet email");
        }

        if (user.isEmpty()) {
            OidcUserInfos oidcUserInfos = new OidcUserInfos(oidcUser, provider);
            userService.createUser(oidcUserInfos);
        }

        log.atDebug().log("Leave loadUser() - return {}", oidcUser);
        return oidcUser;
    }
}

package com.marketplace.security.oauth;

import com.marketplace.entity.User;
import com.marketplace.exception.UserAlreadyExistsException;
import com.marketplace.repository.UserRepository;
import com.marketplace.security.user.OidcUserInfos;
import com.marketplace.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        User user = userRepository.findByEmail(oidcUser.getEmail()).orElse(null);

        if (user != null && !user.getProvider().equals(provider)) {
            throw new UserAlreadyExistsException("Un compte existe déjà avec cet email");
        }

        if (user == null) {
            OidcUserInfos oidcUserInfos = new OidcUserInfos(oidcUser, provider);
            userService.createUser(oidcUserInfos);
        }

        return oidcUser;
    }
}

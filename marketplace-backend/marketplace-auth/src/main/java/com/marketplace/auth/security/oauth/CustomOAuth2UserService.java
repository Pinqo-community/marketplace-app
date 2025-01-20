package com.marketplace.auth.security.oauth;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.exception.AlreadyExistsException;
import com.marketplace.api.service.UserService;
import com.marketplace.auth.model.OAuth2UserInfos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        log.debug("Enter loadUser(oAuth2UserRequest = {})", oAuth2UserRequest);

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        Optional<UserDTO> user = userService.findByEmail(oAuth2User.getAttribute("email"));

        if (user.isPresent() && !user.get().provider().equals(provider)) {
            log.error("An account already exists with this email");
            throw new AlreadyExistsException("Un compte existe déjà avec cet email");
        }

        if (user.isEmpty()) {
            OAuth2UserInfos oAuth2UserInfos = new OAuth2UserInfos(oAuth2User, provider);
            userService.createUser(oAuth2UserInfos);
        }

        log.debug("Leave loadUser() - return {}", oAuth2User);

        return oAuth2User;
    }
}

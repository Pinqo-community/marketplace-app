package com.marketplace.security.oauth;

import com.marketplace.entity.User;
import com.marketplace.exception.UserAlreadyExistsException;
import com.marketplace.repository.UserRepository;
import com.marketplace.security.user.OAuth2UserInfos;
import com.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        User user = userRepository.findByEmail(oAuth2User.getAttribute("email")).orElse(null);

        if (user != null && !user.getProvider().equals(provider)) {
            throw new UserAlreadyExistsException("Un compte existe déjà avec cet email");
        }

        if (user == null) {
            OAuth2UserInfos oAuth2UserInfos = new OAuth2UserInfos(oAuth2User, provider);
            userService.createUser(oAuth2UserInfos);
        }

        return oAuth2User;
    }
}

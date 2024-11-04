package com.marketplace.security.oauth;

import com.marketplace.entity.Buyer;
import com.marketplace.entity.Role;
import com.marketplace.entity.User;
import com.marketplace.exception.UserAlreadyExistsException;
import com.marketplace.model.RoleType;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.RoleRepository;
import com.marketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BuyerRepository buyerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        User user = userRepository.findByEmail(oAuth2User.getAttributes().get("email").toString()).orElse(null);

        if (user != null && !user.getProvider().equals(provider)) {
            throw new UserAlreadyExistsException("User already exists");
        }

        if (user == null) {
            Buyer buyer = buyerRepository.save(
                    Buyer.builder()
                            .firstName(oAuth2User.getAttributes().get("name").toString())
                            .lastName(oAuth2User.getAttributes().get("name").toString())
                            .build()
            );

            User newUser = User.builder()
                    .email(oAuth2User.getAttributes().get("email").toString())
                    .provider(provider)
                    .enabled(true)
                    .buyer(buyer)
                    .build();

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(RoleType.ROLE_USER).orElse(null));

            newUser.setRoles(roles);

            userRepository.save(newUser);
        }

        return oAuth2User;
    }
}

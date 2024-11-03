package com.marketplace.security.oauth;

import com.marketplace.entity.Buyer;
import com.marketplace.entity.Role;
import com.marketplace.entity.User;
import com.marketplace.exception.UserAlreadyExistsException;
import com.marketplace.model.RoleType;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.RoleRepository;
import com.marketplace.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BuyerRepository buyerRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        User user = userRepository.findByEmail(oidcUser.getEmail()).orElse(null);

        if (user != null && !user.getProvider().equals("google")) {
            throw new UserAlreadyExistsException("User already exists");
        }

        if (user == null) {
            Buyer buyer = buyerRepository.save(
                    Buyer.builder()
                            .firstName(oidcUser.getGivenName())
                            .lastName(oidcUser.getFamilyName())
                            .build()
            );

            User newUser = User.builder()
                    .email(oidcUser.getEmail())
                    .provider(provider)
                    .enabled(true)
                    .buyer(buyer)
                    .build();

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(RoleType.ROLE_USER).orElse(null));

            newUser.setRoles(roles);

            userRepository.save(newUser);
        }

        return oidcUser;
    }
}

package com.marketplace.auth.model;

import com.marketplace.api.dto.user.UserInfos;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserInfos implements UserInfos {
    private final OAuth2User oauth2User;
    private final String provider;

    public OAuth2UserInfos(final OAuth2User oauth2User, final String provider) {
        this.oauth2User = oauth2User;
        this.provider = provider;
    }

    @Override
    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getFirstName() {
        String name = oauth2User.getAttribute("name");
        if (name != null) {
            String[] parts = name.split(" ");
            return parts.length > 0 ? parts[0] : "";
        }
        return "";
    }

    @Override
    public String getLastName() {
        String name = oauth2User.getAttribute("name");
        if (name != null) {
            String[] parts = name.split(" ");
            return parts.length > 1 ? parts[parts.length - 1] : "";
        }
        return "";
    }

    @Override
    public String getProvider() {
        return provider;
    }
}

package com.marketplace.security.user;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class OidcUserInfos implements UserInfos {
    private final OidcUser oidcUser;
    private final String provider;

    public OidcUserInfos(final OidcUser oidcUser, final String provider) {
        this.oidcUser = oidcUser;
        this.provider = provider;
    }

    @Override
    public String getEmail() {
        return oidcUser.getEmail();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getFirstName() {
        return oidcUser.getGivenName();
    }

    @Override
    public String getLastName() {
        return oidcUser.getFamilyName();
    }

    @Override
    public String getProvider() {
        return provider;
    }
}

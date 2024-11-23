package com.marketplace.model.user;

import com.marketplace.dto.RegisterRequest;

public class BasicUserInfos implements UserInfos {
    private final RegisterRequest registerRequest;
    private final String provider;

    public BasicUserInfos(final RegisterRequest registerRequest) {
        this.registerRequest = registerRequest;
        this.provider = "local";
    }

    @Override
    public String getEmail() {
        return registerRequest.email();
    }

    @Override
    public String getPassword() {
        return registerRequest.password();
    }

    @Override
    public String getFirstName() {
        return registerRequest.firstname();
    }

    @Override
    public String getLastName() {
        return registerRequest.lastname();
    }

    @Override
    public String getProvider() {
        return provider;
    }
}

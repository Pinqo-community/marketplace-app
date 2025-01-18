package com.marketplace.auth.model;

import com.marketplace.api.dto.UserInfos;
import com.marketplace.api.dto.auth.RegisterRequest;
import lombok.Getter;

public class BasicUserInfos implements UserInfos {
    private final String email;
    private final String password;
    private final String firstname;
    private final String lastname;
    private final String provider;

    public BasicUserInfos(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstname = firstName;
        this.lastname = lastName;
        this.provider = "local";
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getFirstName() {
        return firstname;
    }

    @Override
    public String getLastName() {
        return lastname;
    }

    @Override
    public String getProvider() {
        return provider;
    }
}

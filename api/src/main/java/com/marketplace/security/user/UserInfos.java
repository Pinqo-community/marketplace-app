package com.marketplace.security.user;

import java.util.Map;

public interface UserInfos {
    String getEmail();
    String getPassword();
    String getFirstName();
    String getLastName();
    String getProvider();
}

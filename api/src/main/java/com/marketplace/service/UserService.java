package com.marketplace.service;

import com.marketplace.entity.User;
import com.marketplace.security.user.UserInfos;

public interface UserService {
    User createUser(UserInfos userInfos);
}

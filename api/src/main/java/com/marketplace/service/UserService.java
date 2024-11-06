package com.marketplace.service;

import com.marketplace.entity.User;
import com.marketplace.model.user.UserInfos;

public interface UserService {
    User createUser(UserInfos userInfos);
}

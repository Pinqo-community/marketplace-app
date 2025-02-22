package com.marketplace.auth.service.impl;

import com.marketplace.api.dto.user.UserDTO;
import com.marketplace.api.service.UserService;
import com.marketplace.auth.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    /**
     * Loads user details by email for Spring Security authentication
     *
     * @param email User's email to search for
     * @return UserDetails containing user's security information
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        UserDTO user = userService.getUserByEmail(email);
        return new SecurityUser(user);
    }
}

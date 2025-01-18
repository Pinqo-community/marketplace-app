package com.marketplace.auth.service.impl;

import com.marketplace.api.dto.UserDTO;
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
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDTO user = userService.getUserByEmail(email);
        return new SecurityUser(user);
    }
}

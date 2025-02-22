package com.marketplace.api.dto.user;

import java.util.Set;

public record UserDTO(
        Long id,
        String email,
        String password,
        boolean enabled,
        Set<String> roles,
        String provider
) {}

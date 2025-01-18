package com.marketplace.auth.service;

import com.marketplace.api.dto.UserDTO;
import com.marketplace.api.dto.auth.JwtResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    JwtResponse generateJwtToken(UserDTO user);

    String extractSubject(String token);

    Object extractClaimValue(String token, String claimName);

    Boolean validateToken(String token, UserDetails userDetails);

    Boolean isExpired(String token);

    Date extractExpiration(String token);
}

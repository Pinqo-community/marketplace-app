package com.marketplace.service;

import com.marketplace.dto.JwtResponse;
import com.marketplace.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    JwtResponse generateJwtToken(User user);

    String extractSubject(String token);

    Object extractClaimValue(String token, String claimName);

    Boolean validateToken(String token, UserDetails userDetails);

    Boolean isExpired(String token);

    Date extractExpiration(String token);
}

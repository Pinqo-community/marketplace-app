package com.marketplace.dto;

public record JwtResponse(
        String accessToken,
        String refreshToken
) {
}

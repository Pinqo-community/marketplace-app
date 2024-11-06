package com.marketplace.service.impl;

import com.marketplace.dto.LoginRequest;
import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.User;
import com.marketplace.exception.InvalidTokenException;
import com.marketplace.exception.WrongCredentialException;
import com.marketplace.repository.UserRepository;
import com.marketplace.security.jwt.JwtService;
import com.marketplace.model.user.BasicUserInfos;
import com.marketplace.service.AuthService;
import com.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public User register(RegisterRequest request) {
        BasicUserInfos userInfos = new BasicUserInfos(request);
        return userService.createUser(userInfos);
    }

    @Override
    public User authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new WrongCredentialException("Les identifiants sont invalides"));

        if (!user.isLocalProviderAuthentication()) {
            throw new WrongCredentialException("Les identifiants sont invalides");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User refreshToken(String refreshToken) {
        if (jwtService.isExpired(refreshToken) || !jwtService.extractClaimValue(refreshToken, "typ").equals("Refresh")) {
            throw new InvalidTokenException("Token invalide");
        }

        Long userId = Long.parseLong(jwtService.extractSubject(refreshToken));
        return userRepository.findById(userId).orElseThrow(() -> new InvalidTokenException("Token invalide"));

    }
}

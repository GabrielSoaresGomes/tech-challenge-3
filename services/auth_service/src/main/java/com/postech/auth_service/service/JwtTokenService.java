package com.postech.auth_service.service;

import com.postech.auth_service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class JwtTokenService {
    private final JwtEncoder jwtEncoder;

    public String create(User user) {
        var now = Instant.now();

        var set = JwtClaimsSet.builder()
                .issuer("tech-challenge")
                .issuedAt(now)
                .expiresAt(now.plusMillis(60 * 60 * 1000)) // 1 hour
                .claim("user", user.getId())
                .claim("scope", "user:read")
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(set)).getTokenValue();
    }
}

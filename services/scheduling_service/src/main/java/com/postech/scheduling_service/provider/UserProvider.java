package com.postech.scheduling_service.provider;

import com.postech.scheduling_service.client.UserClient;
import com.postech.scheduling_service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserProvider {
    private final UserClient userClient;

    public UserDto getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !(authentication instanceof JwtAuthenticationToken)) {
            return null;
        }

        var userId = Optional.ofNullable(((JwtAuthenticationToken) authentication).getToken())
                .map(JwtClaimAccessor::getSubject)
                .map(Long::parseLong)
                .orElse(null);
        if (Objects.isNull(userId)) {
            return null;
        }

        return this.getUserById(userId);
    }

    public UserDto getUserById(Long id) {
        return this.userClient.getUser(id);
    }
}

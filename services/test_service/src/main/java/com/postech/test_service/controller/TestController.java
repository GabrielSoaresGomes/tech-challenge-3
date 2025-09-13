package com.postech.test_service.controller;

import com.postech.test_service.dto.UserDto;
import com.postech.test_service.provider.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("test")
@RestController
public class TestController {
    private final UserProvider userProvider;

    @GetMapping
    public UserDto get(JwtAuthenticationToken authenticationToken) {
        return this.userProvider.getCurrentUser();
    }
}

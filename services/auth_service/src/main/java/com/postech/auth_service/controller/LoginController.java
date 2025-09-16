package com.postech.auth_service.controller;

import com.postech.auth_service.dto.LoginRequest;
import com.postech.auth_service.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    @PostMapping
    public String login(@RequestBody LoginRequest credentials) {
        return loginService.validate(credentials.getUsername(), credentials.getPassword());
    }
}

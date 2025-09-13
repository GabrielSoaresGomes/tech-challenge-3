package com.postech.auth_service.controller;

import com.postech.auth_service.dto.UserDto;
import com.postech.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping({"user", "internal/user"})
@RestController
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        return this.service.getUser(id);
    }
}

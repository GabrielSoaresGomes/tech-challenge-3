package com.postech.auth_service.controller;

import com.postech.auth_service.dto.UserDto;
import com.postech.auth_service.use_cases.FindUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("internal/user")
@RestController
public class InternalUserController {
    private final FindUserUseCase findUserUseCase;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id) {
        return this.findUserUseCase.execute(id);
    }
}

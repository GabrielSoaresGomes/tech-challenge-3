package com.postech.auth_service.controller;

import com.postech.auth_service.dto.CreateUserDto;
import com.postech.auth_service.dto.UpdateUserDto;
import com.postech.auth_service.dto.UserDto;
import com.postech.auth_service.use_cases.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("user")
@RestController
public class UserController {
    private final ListUserUseCase listUserUseCase;
    private final FindUserUseCase findUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    @GetMapping
    public List<UserDto> list() {
        return this.listUserUseCase.execute(null);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id) {
        return this.findUserUseCase.execute(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto create(@RequestBody CreateUserDto params) {
        return this.createUserUseCase.execute(params);
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody UpdateUserDto params) {
        params.setId(id);
        return this.updateUserUseCase.execute(params);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        this.deleteUserUseCase.execute(id);
    }
}

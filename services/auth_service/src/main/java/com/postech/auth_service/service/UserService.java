package com.postech.auth_service.service;

import com.postech.auth_service.dto.UserDto;
import com.postech.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public UserDto getUser(Long id) {
        return this.repository.findById(id)
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail()))
                .orElse(null);
    }
}

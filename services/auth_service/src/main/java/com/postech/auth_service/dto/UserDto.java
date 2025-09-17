package com.postech.auth_service.dto;

import com.postech.auth_service.entity.UserRole;

public record UserDto(
        Long id,
        String username,
        String name,
        String email,
        UserRole role
) {
}

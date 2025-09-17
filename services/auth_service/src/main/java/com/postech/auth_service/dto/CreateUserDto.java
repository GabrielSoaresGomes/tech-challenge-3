package com.postech.auth_service.dto;

import com.postech.auth_service.entity.UserRole;
import jakarta.validation.constraints.NotNull;

public record CreateUserDto(
        @NotNull String username,
        @NotNull String password,
        @NotNull String name,
        @NotNull String email,
        @NotNull UserRole role
) {
}

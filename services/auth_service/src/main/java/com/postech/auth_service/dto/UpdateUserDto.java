package com.postech.auth_service.dto;

import com.postech.auth_service.entity.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public final class UpdateUserDto {
    private Long id;
    private @NotNull String username;
    private @NotNull String password;
    private @NotNull String name;
    private @NotNull String email;
    private @NotNull UserRole role;
}

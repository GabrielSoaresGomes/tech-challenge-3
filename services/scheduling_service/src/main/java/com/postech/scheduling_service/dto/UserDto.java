package com.postech.scheduling_service.dto;

import com.postech.scheduling_service.enums.UserRole;

public record UserDto(Long id, String username, String name, String email, UserRole role) {
}

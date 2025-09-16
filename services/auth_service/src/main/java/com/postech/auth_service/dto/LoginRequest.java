package com.postech.auth_service.dto;

public record LoginRequest(
        String username,
        String password
) {
}

package com.postech.auth_service.dto;

public record LoginRequestDto(
        String username,
        String password
) {
}

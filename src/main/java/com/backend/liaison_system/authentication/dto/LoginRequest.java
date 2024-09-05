package com.backend.liaison_system.authentication.dto;

public record LoginRequest(
        String email,
        String password
) {
}

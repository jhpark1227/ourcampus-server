package com.umc.ourcampus.auth.application.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}

package com.example.school.auth.application.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}

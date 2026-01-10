package com.example.school.auth.application.dto.request;

public record LogoutRequest(
        String refreshToken
) {
}

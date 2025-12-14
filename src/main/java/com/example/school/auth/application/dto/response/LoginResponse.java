package com.example.school.auth.application.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}

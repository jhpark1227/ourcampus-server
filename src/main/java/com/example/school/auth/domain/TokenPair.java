package com.example.school.auth.domain;

public record TokenPair(
        String accessToken,
        RefreshToken refreshToken
) {
}

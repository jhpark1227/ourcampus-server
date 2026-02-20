package com.umc.ourcampus.auth.domain;

public record TokenPair(
        String accessToken,
        RefreshToken refreshToken
) {
}

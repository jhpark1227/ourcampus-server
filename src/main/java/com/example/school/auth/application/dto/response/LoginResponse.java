package com.example.school.auth.application.dto.response;

import com.example.school.auth.domain.TokenPair;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
    public static LoginResponse from(TokenPair tokenPair) {
        return new LoginResponse(tokenPair.accessToken(), tokenPair.refreshToken().getValue());
    }
}

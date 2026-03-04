package com.umc.ourcampus.auth.application.dto.response;

import com.umc.ourcampus.auth.domain.TokenPair;

public record AccessTokenReissueResponse(
        String accessToken,
        String refreshToken
) {
    public static AccessTokenReissueResponse from(TokenPair tokenPair) {
        return new AccessTokenReissueResponse(tokenPair.accessToken(), tokenPair.refreshToken().getValue());
    }
}

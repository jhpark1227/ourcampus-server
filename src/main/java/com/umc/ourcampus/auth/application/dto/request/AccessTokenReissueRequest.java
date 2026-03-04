package com.umc.ourcampus.auth.application.dto.request;

public record AccessTokenReissueRequest(
        String refreshToken
) {
}

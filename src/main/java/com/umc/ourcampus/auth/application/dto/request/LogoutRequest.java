package com.umc.ourcampus.auth.application.dto.request;

public record LogoutRequest(
        String refreshToken
) {
}

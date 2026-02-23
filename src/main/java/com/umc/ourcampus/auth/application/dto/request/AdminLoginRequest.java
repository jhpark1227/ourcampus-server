package com.umc.ourcampus.auth.application.dto.request;

public record AdminLoginRequest(
        String loginId,
        String password
) {
}

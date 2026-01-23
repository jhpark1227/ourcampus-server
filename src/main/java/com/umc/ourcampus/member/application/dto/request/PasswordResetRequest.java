package com.umc.ourcampus.member.application.dto.request;

public record PasswordResetRequest(
        String email,
        String password,
        String verificationToken
) {
}

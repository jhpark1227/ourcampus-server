package com.example.school.member.application.dto.request;

public record PasswordResetRequest(
        String email,
        String password,
        String verificationToken
) {
}

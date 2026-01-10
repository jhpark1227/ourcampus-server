package com.example.school.member.application.dto.request;

public record PasswordChangeRequest(
        String oldPassword,
        String newPassword
) {
}

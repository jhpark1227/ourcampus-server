package com.umc.ourcampus.member.application.dto.request;

public record PasswordChangeRequest(
        String oldPassword,
        String newPassword
) {
}

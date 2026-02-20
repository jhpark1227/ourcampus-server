package com.umc.ourcampus.member.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "이메일을 입력해 주세요.")
        String email,
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password,
        @NotBlank(message = "이름을 입력해 주세요.")
        String name,
        String profileImageUrl,
        String studentId,
        long universityId,
        long departmentId,
        @NotBlank(message = "이메일 인증 토큰을 입력해 주세요.")
        String verificationToken
) {
}

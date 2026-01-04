package com.example.school.member.application.dto.request;

import jakarta.validation.constraints.Email;

public record VerificationEmailRequest(
        @Email
        String email
) {
}

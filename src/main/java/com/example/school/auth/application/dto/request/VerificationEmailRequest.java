package com.example.school.auth.application.dto.request;

import jakarta.validation.constraints.Email;

public record VerificationEmailRequest(
        @Email
        String email
) {
}

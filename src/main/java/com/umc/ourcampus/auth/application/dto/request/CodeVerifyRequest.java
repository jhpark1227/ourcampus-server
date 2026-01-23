package com.umc.ourcampus.auth.application.dto.request;

import jakarta.validation.constraints.Email;

public record CodeVerifyRequest(
        @Email
        String email,
        String code
) {
}

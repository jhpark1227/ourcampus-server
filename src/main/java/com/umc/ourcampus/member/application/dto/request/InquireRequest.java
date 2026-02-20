package com.umc.ourcampus.member.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InquireRequest(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}

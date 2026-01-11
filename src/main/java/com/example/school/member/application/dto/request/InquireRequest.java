package com.example.school.member.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InquireRequest(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}

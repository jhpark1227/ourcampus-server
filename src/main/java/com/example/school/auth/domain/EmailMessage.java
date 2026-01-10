package com.example.school.auth.domain;

public record EmailMessage(
        String from,
        String to,
        String subject,
        String content
) {
}

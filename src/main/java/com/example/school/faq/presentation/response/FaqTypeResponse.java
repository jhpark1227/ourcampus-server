package com.example.school.faq.presentation.response;

import com.example.school.faq.domain.FaqType;

public record FaqTypeResponse(
        String name,
        String displayName
) {
    public static FaqTypeResponse from(FaqType type) {
        return new FaqTypeResponse(type.name(), type.getDisplayName());
    }
}

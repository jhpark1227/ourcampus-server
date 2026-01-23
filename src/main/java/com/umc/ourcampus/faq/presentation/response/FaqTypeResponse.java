package com.umc.ourcampus.faq.presentation.response;

import com.umc.ourcampus.faq.domain.FaqType;

public record FaqTypeResponse(
        String name,
        String displayName
) {
    public static FaqTypeResponse from(FaqType type) {
        return new FaqTypeResponse(type.name(), type.getDisplayName());
    }
}

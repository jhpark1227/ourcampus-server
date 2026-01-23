package com.umc.ourcampus.faq.application.dto.response;

import com.umc.ourcampus.faq.domain.Faq;
import com.umc.ourcampus.faq.domain.FaqType;

public record FaqResponse(
        Long id,
        String question,
        String answer,
        FaqType type
) {
    public static FaqResponse from(Faq faq) {
        return new FaqResponse(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getType()
        );
    }
}

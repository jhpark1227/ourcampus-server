package com.example.school.faq.application.dto.response;

import com.example.school.faq.domain.Faq;
import com.example.school.faq.domain.FaqType;

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

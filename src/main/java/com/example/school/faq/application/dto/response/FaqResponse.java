package com.example.school.faq.application.dto.response;

import com.example.school.faq.domain.Faq;
import java.time.LocalDate;

public record FaqResponse(
        Long id,
        String title,
        LocalDate date
) {
    public static FaqResponse from(Faq faq) {
        return new FaqResponse(faq.getId(), faq.getTitle(), faq.getCreatedAt().toLocalDate());
    }
}

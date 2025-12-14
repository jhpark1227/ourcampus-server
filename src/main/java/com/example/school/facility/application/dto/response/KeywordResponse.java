package com.example.school.facility.application.dto.response;

import com.example.school.facility.domain.Keyword;

public record KeywordResponse(
        Long id,
        String name
) {
    public static KeywordResponse from(Keyword keyword) {
        return new KeywordResponse(
                keyword.getId(),
                keyword.getName()
        );
    }
}

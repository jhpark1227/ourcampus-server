package com.example.school.facility.application.dto.response;

import com.example.school.facility.domain.Theme;

public record ThemeResponse(
        Long id, String name
) {
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName());
    }
}

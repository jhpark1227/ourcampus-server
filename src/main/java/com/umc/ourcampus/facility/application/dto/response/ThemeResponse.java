package com.umc.ourcampus.facility.application.dto.response;

import com.umc.ourcampus.facility.domain.Theme;

public record ThemeResponse(
        Long id, String name
) {
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName());
    }
}

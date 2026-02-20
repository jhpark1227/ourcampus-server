package com.umc.ourcampus.university.application.dto.response;

import com.umc.ourcampus.university.domain.University;

public record UniversityResponse(
        Long id, String name
) {
    public static UniversityResponse from(University university) {
        return new UniversityResponse(university.getId(), university.getName());
    }
}

package com.example.school.university.application.dto.response;

import com.example.school.university.domain.University;

public record UniversityResponse(
        Long id, String name
) {
    public static UniversityResponse from(University university) {
        return new UniversityResponse(university.getId(), university.getName());
    }
}

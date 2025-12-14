package com.example.school.facility.application.dto.response;

import com.example.school.facility.domain.HashTag;

public record TagResponse(
        Long id, String name
) {
    public static TagResponse from(HashTag hashTag) {
        return new TagResponse(
                hashTag.getId(),
                hashTag.getName()
        );
    }
}

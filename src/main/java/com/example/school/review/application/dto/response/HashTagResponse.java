package com.example.school.review.application.dto.response;

import com.example.school.review.domain.HashTag;

public record HashTagResponse(
        long id,
        String name
) {
    public static HashTagResponse from(HashTag hashTag) {
        return new HashTagResponse(
                hashTag.getId(),
                hashTag.getName()
        );
    }
}

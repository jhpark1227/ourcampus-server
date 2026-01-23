package com.umc.ourcampus.review.application.dto.response;

import com.umc.ourcampus.review.domain.HashTag;

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

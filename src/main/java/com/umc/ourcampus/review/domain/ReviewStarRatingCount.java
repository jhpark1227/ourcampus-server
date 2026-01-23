package com.umc.ourcampus.review.domain;

public record ReviewStarRatingCount(
        StarRating starRating,
        long count
) {
}

package com.example.school.review.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ReviewRequest(
        @NotNull
        Long reservationId,
        @NotNull
        String content,
        int starRating,
        @NotNull
        List<String> images,
        @NotNull
        List<Long> hashTagIds
) {
}

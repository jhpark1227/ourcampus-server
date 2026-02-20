package com.umc.ourcampus.review.application.dto.response;

import com.umc.ourcampus.review.domain.ReviewStarRatingCount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ReviewStatisticsResponse(
        Map<Integer, Long> starRatings,
        double averageStarRating
) {
    public static ReviewStatisticsResponse of(List<ReviewStarRatingCount> starRatingCounts) {
        Map<Integer, Long> statistics = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            statistics.put(i, 0L);
        }
        long totalCount = 0;
        long weightedSum = 0;
        for (ReviewStarRatingCount ratingCount : starRatingCounts) {
            int starValue = ratingCount.starRating().value();
            long count = ratingCount.count();
            statistics.put(starValue, count);
            totalCount += count;
            weightedSum += starValue * count;
        }
        double averageStarRating = totalCount > 0 ? (double) weightedSum / totalCount : 0.0;
        return new ReviewStatisticsResponse(statistics, averageStarRating);
    }
}
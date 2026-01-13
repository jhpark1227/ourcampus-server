package com.example.school.review.domain;

import com.example.school.facility.domain.ReservableFacility;
import java.util.List;

public interface ReviewRepositoryCustom {
    List<ReviewStarRatingCount> getStarRatingCounts(ReservableFacility facility);
}
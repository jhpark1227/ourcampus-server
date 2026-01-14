package com.example.school.review.domain;

import com.example.school.facility.domain.Facility;
import java.util.List;

public interface ReviewRepositoryCustom {
    List<ReviewStarRatingCount> getStarRatingCounts(Facility facility);
}
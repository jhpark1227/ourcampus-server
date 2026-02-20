package com.umc.ourcampus.review.domain;

import com.umc.ourcampus.facility.domain.Facility;
import java.util.List;

public interface ReviewRepositoryCustom {
    List<ReviewStarRatingCount> getStarRatingCounts(Facility facility);
}
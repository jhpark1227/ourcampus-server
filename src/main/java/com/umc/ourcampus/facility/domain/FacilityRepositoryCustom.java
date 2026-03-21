package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.university.domain.University;
import java.util.List;

public interface FacilityRepositoryCustom {
    List<Facility> findByUniversityAndCategory(University university, FacilityCategory category);

    List<Facility> findTopFacilitiesByHashTag(HashTag hashTag, int limit);
}

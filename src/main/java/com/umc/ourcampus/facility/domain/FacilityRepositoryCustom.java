package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.university.domain.University;
import java.util.List;

public interface FacilityRepositoryCustom {
    List<FacilityAndHashTag> findFacilityAndHashTagIdByHashTags(List<HashTag> hashTags);

    List<Facility> findByUniversityAndCategory(University university, FacilityCategory category);

}

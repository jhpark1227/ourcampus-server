package com.example.school.facility.domain;

import com.example.school.review.domain.HashTag;
import java.util.List;

public interface FacilityRepositoryCustom {
    List<FacilityAndHashTag> findFacilityAndHashTagIdByHashTags(List<HashTag> hashTags);
}

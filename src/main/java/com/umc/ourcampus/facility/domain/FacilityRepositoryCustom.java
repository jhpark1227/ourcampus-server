package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.review.domain.HashTag;
import java.util.List;

public interface FacilityRepositoryCustom {
    List<FacilityAndHashTag> findFacilityAndHashTagIdByHashTags(List<HashTag> hashTags);
}

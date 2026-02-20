package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.review.domain.HashTag;

public record FacilityAndHashTag(
        Facility facility,
        HashTag hashTag
) {
}

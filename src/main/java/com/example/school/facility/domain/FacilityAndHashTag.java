package com.example.school.facility.domain;

import com.example.school.review.domain.HashTag;

public record FacilityAndHashTag(
        Facility facility,
        HashTag hashTag
) {
}

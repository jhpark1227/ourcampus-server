package com.umc.ourcampus.facility.application.dto.response;

import com.umc.ourcampus.facility.domain.Facility;

public record HashTagFacilityResponse(
        long id,
        String name,
        String thumbnailImage
) {
    public static HashTagFacilityResponse from(Facility facility) {
        return new HashTagFacilityResponse(
                facility.getId(),
                facility.getName(),
                facility.getThumbnailImage()
        );
    }
}
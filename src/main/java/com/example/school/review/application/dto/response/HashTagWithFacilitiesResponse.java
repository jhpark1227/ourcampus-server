package com.example.school.review.application.dto.response;

import com.example.school.facility.domain.Facility;
import com.example.school.review.domain.HashTag;
import java.util.List;

public record HashTagWithFacilitiesResponse(
        long id,
        String name,
        List<FacilityResponse> facilities
) {
    public static HashTagWithFacilitiesResponse of(HashTag hashTag, List<Facility> facilities) {
        return new HashTagWithFacilitiesResponse(
                hashTag.getId(),
                hashTag.getName(),
                facilities.stream()
                        .map(FacilityResponse::from)
                        .toList()
        );
    }

    private record FacilityResponse(
            long id,
            String name,
            String thumbnailImage
    ) {
        private static FacilityResponse from(Facility facility) {
            return new FacilityResponse(
                    facility.getId(),
                    facility.getName(),
                    facility.getThumbnailImage()
            );
        }
    }
}

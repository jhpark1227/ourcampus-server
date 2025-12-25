package com.example.school.review.application.dto.response;

import com.example.school.facility.domain.Building;
import com.example.school.facility.domain.Facility;
import com.example.school.review.domain.Review;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record ReviewResponse(
        Long id,
        String content,
        int startRating,
        List<String> images,
        LocalDateTime createdAt,
        FacilityResponse facility
) {
    public static ReviewResponse from(Review review) {
        Facility facility = review.getFacility();
        Optional<Building> building = facility.getBuilding();
        BuildingResponse buildingResponse = building.map(value -> new BuildingResponse(value.getName())).orElse(null);
        FacilityResponse facilityResponse = new FacilityResponse(
                facility.getName(),
                facility.getThumbnailImage(),
                buildingResponse
        );
        return new ReviewResponse(
                review.getId(),
                review.getContent(),
                review.getStarRating().value(),
                review.getImages(),
                review.getCreatedAt(),
                facilityResponse
        );
    }

    private record FacilityResponse(
            String name,
            String imageUrl,
            BuildingResponse building
    ) {
    }

    private record BuildingResponse(
            String name
    ) {
    }
}

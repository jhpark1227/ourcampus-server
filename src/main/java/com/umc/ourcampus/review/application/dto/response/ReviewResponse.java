package com.umc.ourcampus.review.application.dto.response;

import com.umc.ourcampus.facility.domain.Building;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.review.domain.Review;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record ReviewResponse(
        Long id,
        String content,
        int startRating,
        List<String> images,
        LocalDateTime createdAt,
        MemberResponse member,
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
                MemberResponse.from(review.getMember()),
                facilityResponse
        );
    }

    private record MemberResponse(
            String name,
            String profileImageUrl
    ) {
        private static MemberResponse from(Member member) {
            if (member == null) {
                return new MemberResponse("탈퇴한 사용자", null);
            }
            return new MemberResponse(member.getName(), member.getProfileImage());
        }
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

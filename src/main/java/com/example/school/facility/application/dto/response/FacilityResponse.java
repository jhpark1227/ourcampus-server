package com.example.school.facility.application.dto.response;

import com.example.school.facility.domain.Building;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.OperationTime;
import java.util.List;

public record FacilityResponse(
        Long id,
        String name,
        String description,
        String purpose,
        String equipment,
        String caution,
        String location,
        String imageUrl,
        List<String> images,
        List<OperationTimeResponse> operationTime,
        BuildingResponse building,
        boolean reservable
) {
    public static FacilityResponse from(Facility facility) {
        return new FacilityResponse(
                facility.getId(),
                facility.getName(),
                facility.getDescription(),
                facility.getPurpose(),
                facility.getEquipment(),
                facility.getCaution(),
                facility.getLocation(),
                facility.getThumbnailImage(),
                facility.getImages(),
                facility.getOperationTimes()
                        .stream()
                        .map(OperationTimeResponse::from)
                        .toList(),
                facility.getBuilding().isPresent() ? BuildingResponse.from(facility.getBuilding().get()) : null,
                facility.getReservationPolicy().reservable()
        );
    }

    private record OperationTimeResponse(String name,
                                         String startTime,
                                         String endTime
    ) {
        private static OperationTimeResponse from(OperationTime operationTime) {
            return new OperationTimeResponse(
                    operationTime.name(),
                    operationTime.startTime().toDisplayString(),
                    operationTime.endTime().toDisplayString()
            );
        }
    }

    private record BuildingResponse(
            Long id,
            String name,
            double latitude,
            double longitude,
            String label,
            String thumbnailImage
    ) {
        private static BuildingResponse from(Building building) {
            return new BuildingResponse(
                    building.getId(),
                    building.getName(),
                    building.getLatitude(),
                    building.getLongitude(),
                    building.getLabel(),
                    building.getThumbnailImage()
            );
        }
    }
}

package com.umc.ourcampus.facility.application.dto.response;

import com.umc.ourcampus.facility.domain.Building;
import java.util.List;

public record BuildingResponse(
        Long id,
        String name,
        double latitude,
        double longitude,
        String label,
        String thumbnailImage,
        List<String> images,
        List<OperationTimeResponse> operationTimes
) {
    public static BuildingResponse from(Building building) {
        return new BuildingResponse(
                building.getId(),
                building.getName(),
                building.getLatitude(),
                building.getLongitude(),
                building.getLabel(),
                building.getThumbnailImage(),
                building.getImages(),
                building.getOperationTimes()
                        .stream()
                        .map(OperationTimeResponse::from)
                        .toList()
        );
    }
}

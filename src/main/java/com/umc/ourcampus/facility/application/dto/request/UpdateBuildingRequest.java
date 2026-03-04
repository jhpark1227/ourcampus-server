package com.umc.ourcampus.facility.application.dto.request;

import com.umc.ourcampus.facility.domain.MinuteOffset;
import com.umc.ourcampus.facility.domain.OperationTime;
import java.util.List;

public record UpdateBuildingRequest(
        String name,
        String label,
        double latitude,
        double longitude,
        String thumbnailImage,
        List<String> images,
        List<OperationTimeRequest> operationTimes
) {
    public record OperationTimeRequest(
            String name,
            int startTimeMinuteOffset,
            int endTimeMinuteOffset
    ) {
    }

    public List<OperationTime> toOperationTimes() {
        return operationTimes.stream()
                .map(o -> new OperationTime(o.name, new MinuteOffset(o.startTimeMinuteOffset), new MinuteOffset(o.endTimeMinuteOffset)))
                .toList();
    }
}

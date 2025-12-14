package com.example.school.facility.application.dto.response;

import com.example.school.facility.domain.OperationTime;
import java.time.LocalTime;

public record OperationTimeResponse(
        String name,
        LocalTime startTime,
        LocalTime endTime
) {
    public static OperationTimeResponse from(OperationTime operationTime) {
        return new OperationTimeResponse(
                operationTime.name(),
                operationTime.startTime(),
                operationTime.endTime()
        );
    }
}

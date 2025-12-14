package com.example.school.facility.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
public record OperationTime(
        String name,
        LocalTime startTime,
        LocalTime endTime
) {
}

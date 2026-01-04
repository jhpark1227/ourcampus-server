package com.example.school.facility.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record OperationTime(
        String name,
        @AttributeOverride(name = "value", column = @Column(name = "start_time"))
        MinuteOffset startTime,
        @AttributeOverride(name = "value", column = @Column(name = "end_time"))
        MinuteOffset endTime
) {
}

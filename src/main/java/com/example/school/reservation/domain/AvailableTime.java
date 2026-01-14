package com.example.school.reservation.domain;

import com.example.school.facility.domain.MinuteOffset;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record AvailableTime(
        @AttributeOverride(name = "value", column = @Column(name = "start_time"))
        MinuteOffset startTime,
        @AttributeOverride(name = "value", column = @Column(name = "end_time"))
        MinuteOffset endTime
) {
}

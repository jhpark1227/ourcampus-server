package com.umc.ourcampus.reservation.domain;

import com.umc.ourcampus.facility.domain.MinuteOffset;
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

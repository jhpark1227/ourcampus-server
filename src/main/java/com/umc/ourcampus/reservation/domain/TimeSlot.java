package com.umc.ourcampus.reservation.domain;

import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import jakarta.persistence.Embeddable;
import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
public record TimeSlot(
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static TimeSlot of(LocalDateTime startTime, Duration duration) {
        return new TimeSlot(startTime, startTime.plus(duration));
    }

    public boolean overlaps(TimeSlot other) {
        return !(
                endTime.isBefore(other.startTime) ||
                        endTime.equals(other.startTime) ||
                        this.startTime.isAfter(other.endTime) ||
                        this.startTime.equals(other.endTime)
        );
    }

    public boolean isDivisibleBy(Duration unitDuration) {
        Duration thisDuration = Duration.between(startTime, endTime);
        return thisDuration.getSeconds() % unitDuration.getSeconds() == 0;
    }

    public boolean isLongerThan(Duration durationLimit) {
        Duration thisDuration = Duration.between(startTime, endTime);
        return thisDuration.compareTo(durationLimit) > 0;
    }

    public LocalDateTime getTimeBefore(Duration duration) {
        return startTime.minus(duration);
    }

    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    public TimeSlot extend(LocalDateTime newEndTime) {
        if (!newEndTime.isAfter(endTime)) {
            throw new ApplicationException(ErrorStatus.EXTENDED_TIME_ERROR);
        }
        return new TimeSlot(startTime, newEndTime);
    }
}

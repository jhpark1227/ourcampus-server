package com.umc.ourcampus.facility.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Embeddable
public record MinuteOffset(int value) {
    public MinuteOffset {
        if (value < 0 || value > 1440) {
            throw new IllegalArgumentException("0에서 1440분 사이여야 합니다.");
        }
    }

    public LocalDateTime toDateTime(LocalDate date) {
        return date.atStartOfDay().plusMinutes(value);
    }

    public String toDisplayString() {
        return String.format("%02d:%02d", value / 60, value % 60);
    }
}
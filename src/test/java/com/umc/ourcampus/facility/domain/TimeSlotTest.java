package com.umc.ourcampus.facility.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.umc.ourcampus.reservation.domain.TimeSlot;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TimeSlotTest {

    @Test
    void 중복된_시간이_30분이면_겹친다() {
        TimeSlot timeSlot1 = new TimeSlot(
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2025, 1, 1, 10, 0)
        );
        TimeSlot timeSlot2 = new TimeSlot(
                LocalDateTime.of(2025, 1, 1, 9, 30),
                LocalDateTime.of(2025, 1, 1, 10, 30)
        );

        boolean actual = timeSlot1.overlaps(timeSlot2);

        assertThat(actual).isTrue();
    }

    @Test
    void 이어진_시간은_겹치지_않는다() {
        TimeSlot timeSlot1 = new TimeSlot(
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2025, 1, 1, 10, 0)
        );
        TimeSlot timeSlot2 = new TimeSlot(
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 11, 0)
        );

        boolean actual = timeSlot1.overlaps(timeSlot2);

        assertThat(actual).isFalse();
    }

    @Test
    void 떨어진_시간은_겹치지_않는다() {
        TimeSlot timeSlot1 = new TimeSlot(
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2025, 1, 1, 10, 0)
        );
        TimeSlot timeSlot2 = new TimeSlot(
                LocalDateTime.of(2025, 1, 1, 11, 0),
                LocalDateTime.of(2025, 1, 1, 12, 0)
        );

        boolean actual = timeSlot1.overlaps(timeSlot2);

        assertThat(actual).isFalse();
    }
}
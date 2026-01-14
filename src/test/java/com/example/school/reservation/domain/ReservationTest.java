package com.example.school.reservation.domain;

import com.example.school.facility.domain.Facility;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Email;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.Password;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void 예약_인원이_1미만이면_예외가_발생한다() {
        Facility facility = new Facility(

        )
        Member member = Member.create(
                        "이름",
                        new Email("email@email.com"),
                        new Password(""),
                        "profileImageUrl",
                "20202020",

                )
        LocalDateTime now = LocalDateTime.now();
        Assertions.assertThatThrownBy(() -> {
            Reservation.builder()
                    .facility(facility)
                    .member(member)
                    .headCount(0)
                    .timeSlot(new TimeSlot(now, now.plus(Duration.ofMinutes(60))))
                    .images(List.of())
                    .build();
        }).isInstanceOf(ApplicationException.class);
    }
}
package com.umc.ourcampus.reservation.domain;

import com.umc.ourcampus.alarm.domain.Alarm;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("RESERVATION")
public class ReservationAlarm extends Alarm {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    private AlarmTiming alarmTiming;

    public ReservationAlarm(Reservation reservation, AlarmTiming alarmTiming) {
        super(
                reservation.getMember(),
                "예약 알림",
                reservation.getFacility().getName() + "예약" + alarmTiming.getDisplayName() + "입니다.",
                reservation.calculateScheduledTime(alarmTiming)
        );
        this.reservation = reservation;
        this.alarmTiming = alarmTiming;
    }

    @Override
    public String getType() {
        return "RESERVATION";
    }
}

package com.example.school.facility.domain;

import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.domain.TimeSlot;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("RESERVABLE")
public class ReservableFacility extends Facility {

    private static final Duration RESERVATION_TIME_UNIT = Duration.ofMinutes(60);
    private static final Duration RESERVATION_DURATION_LIMIT = RESERVATION_TIME_UNIT.multipliedBy(3);

    public List<TimeSlot> getTimeSlots(LocalDate date) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (OperationTime operationTime : operationTimes) {
            LocalDateTime operationStartTime = operationTime.startTime().toDateTime(date);
            LocalDateTime operationEndTime = operationTime.endTime().toDateTime(date);
            LocalDateTime reservationStartTime = operationStartTime;
            while (true) {
                LocalDateTime reservationEndTime = reservationStartTime.plus(RESERVATION_TIME_UNIT);
                if (reservationEndTime.isAfter(operationEndTime) || reservationEndTime.isBefore(reservationStartTime)) {
                    break;
                }
                timeSlots.add(new TimeSlot(reservationStartTime, reservationEndTime));
                reservationStartTime = reservationStartTime.plus(RESERVATION_TIME_UNIT);
            }
        }
        return timeSlots;
    }

    public Collection<TimeSlot> getAvailableTimeSlots(LocalDate date, List<Reservation> reservations) {
        return getTimeSlots(date).stream()
                .filter(timeSlot -> reservations.stream().noneMatch(reservation -> reservation.overlapTimeSlot(timeSlot)))
                .collect(Collectors.toSet());
    }

    public void isValidSlot(TimeSlot timeSlot) {
        LocalDate date = timeSlot.startTime().toLocalDate();
        if (getTimeSlots(date).stream().noneMatch(t -> t.startTime().equals(timeSlot.startTime()))) {
            throw new ApplicationException(ErrorStatus.INVALID_TIMESLOT);
        }
        if (!timeSlot.isDivisibleBy(RESERVATION_TIME_UNIT)) {
            throw new ApplicationException(ErrorStatus.INVALID_TIMESLOT);
        }
        if (timeSlot.isLongerThan(RESERVATION_DURATION_LIMIT)) {
            throw new ApplicationException(ErrorStatus.INVALID_TIMESLOT);
        }
        boolean isWithinOperationTime = operationTimes.stream()
                .anyMatch(operationTime -> {
                    LocalDateTime opStart = operationTime.startTime().toDateTime(date);
                    LocalDateTime opEnd = operationTime.endTime().toDateTime(date);
                    return !timeSlot.startTime().isBefore(opStart) &&
                            !timeSlot.endTime().isAfter(opEnd);
                });
        if (!isWithinOperationTime) {
            throw new ApplicationException(ErrorStatus.INVALID_TIMESLOT);
        }
    }

    public Duration getReservationDurationLimit() {
        return RESERVATION_DURATION_LIMIT;
    }

    @Override
    public boolean reservable() {
        return true;
    }
}

package com.umc.ourcampus.reservation.domain;

import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.member.domain.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    private final ReservationRepository reservationRepository;

    public void validate(Member member, Facility facility, TimeSlot timeSlot) {
        List<Reservation> reservations = reservationRepository.findByFacilityAndDate(
                facility,
                timeSlot.startTime().toLocalDate()
        );

        validateTimeSlotOverlap(reservations, timeSlot);
        validateDailyLimitPerFacility(reservations, member);
    }

    public void validateForExtension(Facility facility, TimeSlot extendedTimeSlot, Reservation currentReservation) {
        List<Reservation> reservations = new ArrayList<>(reservationRepository.findByFacilityAndDate(
                facility,
                extendedTimeSlot.startTime().toLocalDate()
        ));
        reservations.remove(currentReservation);

        validateTimeSlotOverlap(reservations, extendedTimeSlot);
    }

    private void validateTimeSlotOverlap(List<Reservation> reservations, TimeSlot timeSlot) {
        if (reservations.stream().anyMatch(reservation -> reservation.overlapTimeSlot(timeSlot))) {
            throw new ApplicationException(ErrorStatus.TIMESLOT_OVERLAP);
        }
    }

    private void validateDailyLimitPerFacility(List<Reservation> reservations, Member member) {
        boolean alreadyReserved = reservations.stream()
                .anyMatch(reservation -> reservation.getMember().equals(member));
        if (alreadyReserved) {
            throw new ApplicationException(ErrorStatus.FACILITY_DAILY_LIMIT_EXCEEDED);
        }
    }
}

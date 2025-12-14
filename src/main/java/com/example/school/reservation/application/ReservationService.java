package com.example.school.reservation.application;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.reservation.application.dto.request.ReservationRequest;
import com.example.school.reservation.application.dto.response.ReservationCreateResponse;
import com.example.school.reservation.application.dto.response.ReservationResponse;
import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.domain.ReservationAlarm;
import com.example.school.reservation.domain.ReservationAlarmRepository;
import com.example.school.reservation.domain.ReservationRepository;
import com.example.school.reservation.domain.TimeSlot;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final FacilityRepository facilityRepository;
    private final ReservationAlarmRepository reservationAlarmRepository;
    private final SimpMessagingTemplate template;

    @Transactional
    public ReservationCreateResponse createReservation(ReservationRequest request, MemberPrincipal memberPrincipal) {
        Facility facility = facilityRepository.findById(request.facilityId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        Member member = memberRepository.findById(memberPrincipal.memberId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Reservation> reservations = reservationRepository.findByFacilityAndDate(facility, request.startTime().toLocalDate());
        TimeSlot timeSlot = new TimeSlot(request.startTime(), request.startTime().plus(Duration.ofMinutes(request.durationMinutes())));
        validateTimeSlotOverlap(reservations, timeSlot);
        validateInvalidTimeSlot(facility, timeSlot, request.startTime().toLocalDate());

        Reservation reservation = Reservation.builder()
                .facility(facility)
                .member(member)
                .timeSlot(timeSlot)
                .headCount(request.headCount())
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);

        List<ReservationAlarm> alarms = request.alarmTimings().stream()
                .map(alarmTiming -> new ReservationAlarm(reservation, alarmTiming))
                .toList();
        reservationAlarmRepository.saveAll(alarms);

        return ReservationCreateResponse.from(savedReservation);
    }

    private void validateInvalidTimeSlot(Facility facility, TimeSlot timeSlot, LocalDate date) {
        if (!facility.isValidSlot(date, timeSlot)) {
            throw new ApplicationException(ErrorStatus.INVALID_TIMESLOT);
        }
    }

    private void validateTimeSlotOverlap(List<Reservation> reservations, TimeSlot timeSlot) {
        if (reservations.stream().anyMatch(reservation -> reservation.overlapTimeSlot(timeSlot))) {
            throw new ApplicationException(ErrorStatus.TIMESLOT_OVERLAP);
        }
    }

    public List<ReservationResponse> findReservationsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Reservation> reservations = reservationRepository.findByMember(member);

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
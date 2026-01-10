package com.example.school.reservation.application;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.reservation.application.dto.request.ReservationExtendRequest;
import com.example.school.reservation.application.dto.request.ReservationRequest;
import com.example.school.reservation.application.dto.request.ReservationReturnRequest;
import com.example.school.reservation.application.dto.response.ReservationCreateResponse;
import com.example.school.reservation.application.dto.response.ReservationResponse;
import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.domain.ReservationAlarm;
import com.example.school.reservation.domain.ReservationAlarmRepository;
import com.example.school.reservation.domain.ReservationRepository;
import com.example.school.reservation.domain.TimeSlot;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final FacilityRepository facilityRepository;
    private final ReservationAlarmRepository reservationAlarmRepository;

    public ReservationCreateResponse createReservation(ReservationRequest request, MemberPrincipal memberPrincipal) {
        Facility facility = facilityRepository.findById(request.facilityId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        Member member = memberRepository.findById(memberPrincipal.memberId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Reservation> reservations = reservationRepository.findByFacilityAndDate(facility, request.startTime().toLocalDate());

        TimeSlot timeSlot = new TimeSlot(request.startTime(), request.startTime().plus(Duration.ofMinutes(request.durationMinutes())));
        validateTimeSlotOverlap(reservations, timeSlot);
        validateDailyLimitPerFacility(reservations, member);

        Reservation reservation = Reservation.builder()
                .facility(facility)
                .member(member)
                .timeSlot(timeSlot)
                .headCount(request.headCount())
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);

        List<ReservationAlarm> alarms = request.alarmTimings()
                .stream()
                .map(alarmTiming -> new ReservationAlarm(reservation, alarmTiming))
                .toList();
        reservationAlarmRepository.saveAll(alarms);

        return ReservationCreateResponse.from(savedReservation);
    }

    public List<ReservationResponse> findReservationsByMemberId(Long memberId, boolean onlyPendingReview) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        if (onlyPendingReview) {
            return reservationRepository.findReturnedReservationsWithoutReviewByMember(member)
                    .stream()
                    .map(ReservationResponse::from)
                    .toList();
        }
        return reservationRepository.findByMember(member)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse findReservationById(Long reservationId, Long memberId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.RESERVATION_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow((() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND)));
        reservation.validateOwner(member);

        return ReservationResponse.from(reservation);
    }

    public ReservationResponse findInUseReservation(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        return reservationRepository.findInUseReservationByMember(member)
                .map(ReservationResponse::from)
                .orElse(null);
    }

    public void extendReservation(long reservationId, ReservationExtendRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.RESERVATION_NOT_FOUND));
        List<Reservation> reservations = reservationRepository.findByFacilityAndDate(reservation.getFacility(),
                reservation.getTimeSlot().startTime().toLocalDate());
        reservations.remove(reservation);

        TimeSlot extendedTimeSlot = reservation.getTimeSlot().extend(request.endTime());
        validateTimeSlotOverlap(reservations, extendedTimeSlot);
        reservation.validateOwner(member);

        reservation.extend(request.endTime());
    }

    public ReservationResponse returnReservation(long reservationId, ReservationReturnRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.RESERVATION_NOT_FOUND));
        reservation.validateOwner(member);

        reservation.markAsReturned(request.images());

        return ReservationResponse.from(reservation);
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
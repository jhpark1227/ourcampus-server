package com.umc.ourcampus.reservation.application;

import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.reservation.application.dto.response.TimeSlotWithBookedResponse;
import com.umc.ourcampus.facility.application.dto.response.FacilityScheduleResponse;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.member.domain.MemberRepository;
import com.umc.ourcampus.reservation.application.dto.request.ReservationExtendRequest;
import com.umc.ourcampus.reservation.application.dto.request.ReservationRequest;
import com.umc.ourcampus.reservation.application.dto.request.ReservationReturnRequest;
import com.umc.ourcampus.reservation.application.dto.response.ReservationCreateResponse;
import com.umc.ourcampus.reservation.application.dto.response.ReservationInfo;
import com.umc.ourcampus.reservation.application.dto.response.ReservationResponse;
import com.umc.ourcampus.reservation.domain.AlarmRepository;
import com.umc.ourcampus.reservation.domain.HeadCount;
import com.umc.ourcampus.reservation.domain.Reservation;
import com.umc.ourcampus.reservation.domain.ReservationAlarm;
import com.umc.ourcampus.reservation.domain.ReservationPolicy;
import com.umc.ourcampus.reservation.domain.ReservationRepository;
import com.umc.ourcampus.reservation.domain.TimeSlot;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
    private final AlarmRepository alarmRepository;

    public ReservationCreateResponse createReservation(ReservationRequest request, long memberId) {
        Facility facility = facilityRepository.findById(request.facilityId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Reservation> reservations = reservationRepository.findByFacilityAndDate(facility, request.startTime().toLocalDate());

        TimeSlot timeSlot = new TimeSlot(request.startTime(), request.startTime().plus(Duration.ofMinutes(request.durationMinutes())));
        validateTimeSlotOverlap(reservations, timeSlot);
        validateDailyLimitPerFacility(reservations, member);

        Reservation reservation = Reservation.create(
                timeSlot,
                new HeadCount(request.headCount()),
                member,
                facility
        );
        Reservation savedReservation = reservationRepository.save(reservation);

        List<ReservationAlarm> alarms = request.alarmTimings()
                .stream()
                .map(alarmTiming -> new ReservationAlarm(reservation, alarmTiming))
                .toList();
        alarmRepository.saveAll(alarms);

        return ReservationCreateResponse.from(savedReservation);
    }

    public List<ReservationResponse> findReservationsByMember(Long memberId, boolean onlyPendingReview) {
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

    public List<FacilityScheduleResponse> getWeeklySchedule(Long facilityId, LocalDate baseDate) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        List<FacilityScheduleResponse> facilitySchedules = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = baseDate.plusDays(i);
            List<TimeSlotWithBookedResponse> times = getAvailableTimeSlots(facility, date);
            facilitySchedules.add(new FacilityScheduleResponse(times, date));
        }
        return facilitySchedules;
    }

    public ReservationInfo getReservationInfo(long facilityId, LocalDate date) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        ReservationPolicy reservationPolicy = facility.getReservationPolicy();
        return ReservationInfo.of(getAvailableTimeSlots(facility, date), reservationPolicy.getReservationDurationLimit());
    }

    private List<TimeSlotWithBookedResponse> getAvailableTimeSlots(Facility facility, LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByFacilityAndDate(facility, date);
        ReservationPolicy reservationPolicy = facility.getReservationPolicy();
        List<TimeSlot> timeSlots = reservationPolicy.getTimeSlots(date);
        Collection<TimeSlot> availableTimeSlots = reservationPolicy.getAvailableTimeSlots(date, reservations);
        return timeSlots.stream()
                .map(timeSlot -> TimeSlotWithBookedResponse.of(timeSlot, !availableTimeSlots.contains(timeSlot)))
                .toList();
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
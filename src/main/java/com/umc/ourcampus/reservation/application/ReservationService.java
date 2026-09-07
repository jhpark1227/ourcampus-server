package com.umc.ourcampus.reservation.application;

import com.umc.ourcampus.facility.application.dto.response.FacilityScheduleResponse;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.member.domain.MemberRepository;
import com.umc.ourcampus.reservation.application.dto.request.ReservationExtendRequest;
import com.umc.ourcampus.reservation.application.dto.request.ReservationRequest;
import com.umc.ourcampus.reservation.application.dto.request.ReservationReturnRequest;
import com.umc.ourcampus.reservation.application.dto.response.ReservationCreateResponse;
import com.umc.ourcampus.reservation.application.dto.response.ReservationInfo;
import com.umc.ourcampus.reservation.application.dto.response.ReservationResponse;
import com.umc.ourcampus.reservation.domain.HeadCount;
import com.umc.ourcampus.reservation.domain.Reservation;
import com.umc.ourcampus.reservation.domain.ReservationPolicy;
import com.umc.ourcampus.reservation.domain.ReservationRepository;
import com.umc.ourcampus.reservation.domain.ReservationValidator;
import com.umc.ourcampus.reservation.domain.TimeSlot;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private static final int WEEKLY_SCHEDULE_DAYS = 7;

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final FacilityRepository facilityRepository;
    private final ReservationValidator reservationValidator;
    private final ReservationLock reservationLock;

    @Transactional
    public ReservationCreateResponse createReservation(ReservationRequest request, long memberId) {
        reservationLock.getLock(request.facilityId(), request.startTime().toLocalDate());
        Facility facility = facilityRepository.findById(request.facilityId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        TimeSlot timeSlot = TimeSlot.of(request.startTime(), Duration.ofMinutes(request.durationMinutes()));

        reservationValidator.validate(member, facility, timeSlot);

        Reservation reservation = Reservation.create(
                timeSlot,
                new HeadCount(request.headCount()),
                member,
                facility
        );
        reservation.addAlarms(request.alarmTimings());
        Reservation savedReservation = reservationRepository.save(reservation);
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
        ReservationPolicy reservationPolicy = facility.getReservationPolicy();
        Map<LocalDate, List<Reservation>> reservationsByDate = reservationRepository
                .findByFacilityAndDateRange(facility, baseDate, baseDate.plusDays(WEEKLY_SCHEDULE_DAYS))
                .stream()
                .collect(Collectors.groupingBy(reservation -> reservation.getTimeSlot().startTime().toLocalDate()));

        return IntStream.range(0, WEEKLY_SCHEDULE_DAYS)
                .mapToObj(baseDate::plusDays)
                .map(date -> FacilityScheduleResponse.of(
                        date,
                        reservationPolicy.getSlotAvailabilities(
                                date, reservationsByDate.getOrDefault(date, List.of()))))
                .toList();
    }

    public ReservationInfo getReservationInfo(long facilityId, LocalDate date) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        ReservationPolicy reservationPolicy = facility.getReservationPolicy();
        List<Reservation> reservations = reservationRepository.findByFacilityAndDate(facility, date);
        return ReservationInfo.of(
                reservationPolicy.getSlotAvailabilities(date, reservations),
                reservationPolicy.getReservationDurationLimit());
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

        TimeSlot extendedTimeSlot = reservation.getTimeSlot().extend(request.endTime());
        reservationValidator.validateForExtension(reservation.getFacility(), extendedTimeSlot, reservation);
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
}
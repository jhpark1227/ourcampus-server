package com.umc.ourcampus.reservation.presentation;

import com.umc.ourcampus.reservation.application.dto.request.ReservationRequest;
import com.umc.ourcampus.reservation.application.dto.response.ReservationResponse;
import com.umc.ourcampus.auth.domain.MemberPrincipal;
import com.umc.ourcampus.facility.application.dto.response.FacilityScheduleResponse;
import com.umc.ourcampus.reservation.application.ReservationService;
import com.umc.ourcampus.reservation.application.dto.request.ReservationExtendRequest;
import com.umc.ourcampus.reservation.application.dto.request.ReservationReturnRequest;
import com.umc.ourcampus.reservation.application.dto.response.ReservationCreateResponse;
import com.umc.ourcampus.reservation.application.dto.response.ReservationInfo;
import com.umc.ourcampus.reservation.domain.AlarmTiming;
import com.umc.ourcampus.reservation.presentation.dto.AlertOptionsResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public ResponseEntity<ReservationCreateResponse> reserve(
            @RequestBody @Valid ReservationRequest request,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        ReservationCreateResponse response = reservationService.createReservation(request, memberPrincipal.memberId());
        return ResponseEntity.created(URI.create("/reservations/" + response.id()))
                .body(response);
    }

    @GetMapping("/reservations/{reservationId}")
    public ReservationResponse getReservation(
            @PathVariable("reservationId") Long reservationId,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        return reservationService.findReservationById(reservationId, memberPrincipal.memberId());
    }

    @GetMapping("/facilities/{facilityId}/reservation-info")
    public ReservationInfo getReservationInfo(
            @PathVariable("facilityId") long facilityId, @RequestParam LocalDate date
    ) {
        return reservationService.getReservationInfo(facilityId, date);
    }

    @GetMapping("/facilities/{facilityId}/weekly-schedule")
    public List<FacilityScheduleResponse> getWeeklySchedule(
            @PathVariable(name = "facilityId") Long facilityId,
            @RequestParam(name = "date") LocalDate date
    ) {
        return reservationService.getWeeklySchedule(facilityId, date);
    }


    @GetMapping("/reservations/alert-options")
    public List<AlertOptionsResponse> getAlertOptions() {
        return Arrays.stream(AlarmTiming.values())
                .map(AlertOptionsResponse::from)
                .toList();
    }

    @GetMapping("/me/reservations")
    public List<ReservationResponse> getMyReservations(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestParam(name = "onlyPendingReview", required = false) boolean onlyPendingReview
    ) {
        return reservationService.findReservationsByMember(memberPrincipal.memberId(), onlyPendingReview);
    }

    @GetMapping("/me/reservations/in-use")
    public ReservationResponse getInUseReservation(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return reservationService.findInUseReservation(memberPrincipal.memberId());
    }

    @PatchMapping("/reservations/{reservationId}/extend")
    public ResponseEntity<Void> extendReservation(
            @PathVariable("reservationId") long reservationId,
            @RequestBody @Valid ReservationExtendRequest request,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        reservationService.extendReservation(reservationId, request, memberPrincipal.memberId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reservations/{reservationId}/return")
    public ReservationResponse returnReservation(
            @PathVariable("reservationId") long reservationId,
            @RequestBody @Valid ReservationReturnRequest request,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        return reservationService.returnReservation(reservationId, request, memberPrincipal.memberId());
    }
}

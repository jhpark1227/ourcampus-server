package com.example.school.reservation.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.reservation.application.ReservationService;
import com.example.school.reservation.application.dto.request.ReservationRequest;
import com.example.school.reservation.application.dto.response.ReservationCreateResponse;
import com.example.school.reservation.application.dto.response.ReservationResponse;
import com.example.school.reservation.domain.AlarmTiming;
import com.example.school.reservation.presentation.dto.AlertOptionsResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        ReservationCreateResponse response = reservationService.createReservation(request, memberPrincipal);
        return ResponseEntity.created(URI.create("/reservations/" + response.id()))
                .body(response);
    }

    @GetMapping("/reservations/alert-options")
    public List<AlertOptionsResponse> getAlertOptions() {
        return Arrays.stream(AlarmTiming.values())
                .map(AlertOptionsResponse::from)
                .toList();
    }

    @GetMapping("/me/reservations")
    public List<ReservationResponse> getMyReservations(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return reservationService.findReservationsByMemberId(memberPrincipal.memberId());
    }
}

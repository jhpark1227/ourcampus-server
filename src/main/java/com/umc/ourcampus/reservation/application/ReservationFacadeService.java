package com.umc.ourcampus.reservation.application;

import com.umc.ourcampus.reservation.application.dto.request.ReservationRequest;
import com.umc.ourcampus.reservation.domain.NamedLockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationFacadeService {

    private final ReservationService reservationService;
    private final NamedLockRepository namedLockRepository;

    @Transactional
    public void createReservation(ReservationRequest request, long memberId) {
        String key = "reservation:" + request.facilityId() + ":" + request.startTime().toLocalDate();
        namedLockRepository.getLock(key);
        try {
            reservationService.createReservation(request, memberId);
        } finally {
            namedLockRepository.releaseLock(key);
        }
    }
}

package com.umc.ourcampus.reservation.application;

import java.time.LocalDate;

public interface ReservationLock {
    void getLock(long facilityId, LocalDate date);
}

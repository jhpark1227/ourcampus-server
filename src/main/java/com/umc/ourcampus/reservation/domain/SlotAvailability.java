package com.umc.ourcampus.reservation.domain;

public record SlotAvailability(
        TimeSlot timeSlot,
        boolean booked
) {
}
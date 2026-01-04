package com.example.school.facility.domain;

import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.domain.BaseEntity;
import com.example.school.global.exception.ApplicationException;
import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.domain.TimeSlot;
import com.example.school.university.domain.University;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Facility extends BaseEntity {

    private static final Duration RESERVATION_TIME_UNIT = Duration.ofMinutes(60);
    private static final Duration RESERVATION_DURATION_LIMIT = RESERVATION_TIME_UNIT.multipliedBy(3);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String purpose;

    private String equipment;

    private String caution;

    private String location;

    private String thumbnailImage;

    @Enumerated(EnumType.STRING)
    private FacilityCategory category;

    @ElementCollection
    @Builder.Default
    private List<OperationTime> operationTimes = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private University university;

    public List<TimeSlot> getTimeSlots(LocalDate date) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (OperationTime operationTime : operationTimes) {
            LocalDateTime operationStartTime = operationTime.startTime().toDateTime(date);
            LocalDateTime operationEndTime = operationTime.endTime().toDateTime(date);
            LocalDateTime reservationStartTime = operationStartTime;
            while (true) {
                LocalDateTime reservationEndTime = reservationStartTime.plus(RESERVATION_TIME_UNIT);
                if (reservationEndTime.isAfter(operationEndTime) || reservationEndTime.isBefore(reservationStartTime)) {
                    break;
                }
                timeSlots.add(new TimeSlot(reservationStartTime, reservationEndTime));
                reservationStartTime = reservationStartTime.plus(RESERVATION_TIME_UNIT);
            }
        }
        return timeSlots;
    }

    public Collection<TimeSlot> getAvailableTimeSlots(LocalDate date, List<Reservation> reservations) {
        return getTimeSlots(date).stream()
                .filter(timeSlot -> reservations.stream().noneMatch(reservation -> reservation.overlapTimeSlot(timeSlot)))
                .collect(Collectors.toSet());
    }

    public void isValidSlot(TimeSlot timeSlot) {
        LocalDate date = timeSlot.startTime().toLocalDate();
        if (getTimeSlots(date).stream().noneMatch(t -> t.startTime().equals(timeSlot.startTime()))) {
            throw new ApplicationException(ErrorStatus.INVALID_TIMESLOT);
        }
        if (!timeSlot.isDivisibleBy(RESERVATION_TIME_UNIT)) {
            throw new ApplicationException(ErrorStatus.INVALID_TIMESLOT);
        }
        if (timeSlot.isLongerThan(RESERVATION_DURATION_LIMIT)) {
            throw new ApplicationException(ErrorStatus.INVALID_TIMESLOT);
        }
        boolean isWithinOperationTime = operationTimes.stream()
                .anyMatch(operationTime -> {
                    LocalDateTime opStart = operationTime.startTime().toDateTime(date);
                    LocalDateTime opEnd = operationTime.endTime().toDateTime(date);
                    return !timeSlot.startTime().isBefore(opStart) &&
                            !timeSlot.endTime().isAfter(opEnd);
                });
        if (!isWithinOperationTime) {
            throw new ApplicationException(ErrorStatus.INVALID_TIMESLOT);
        }
    }

    public Duration getReservationDurationLimit() {
        return RESERVATION_DURATION_LIMIT;
    }

    public Optional<Building> getBuilding() {
        return Optional.ofNullable(building);
    }
}

package com.umc.ourcampus.reservation.domain;

import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.global.domain.BaseEntity;
import com.umc.ourcampus.member.domain.Member;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE reservation SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TimeSlot timeSlot;

    private HeadCount headCount;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.RESERVED;

    @ElementCollection
    List<String> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    public static Reservation create(TimeSlot timeSlot, HeadCount headCount, Member member, Facility facility) {
        facility.getReservationPolicy().isValidSlot(timeSlot);
        Reservation reservation = new Reservation();
        reservation.timeSlot = timeSlot;
        reservation.headCount = headCount;
        reservation.member = member;
        reservation.facility = facility;
        return reservation;
    }

    public boolean overlapTimeSlot(TimeSlot timeSlot) {
        return this.timeSlot.overlaps(timeSlot);
    }

    public void validateOwner(Member member) {
        if (this.member.equals(member)) {
            return;
        }
        throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
    }

    public LocalDateTime calculateScheduledTime(AlarmTiming alarmTiming) {
        return timeSlot.getTimeBefore(alarmTiming.getDuration());
    }

    public void extend(LocalDateTime endTime) {
        TimeSlot newTimeSlot = timeSlot.extend(endTime);
        facility.getReservationPolicy().isValidSlot(timeSlot);
        this.timeSlot = newTimeSlot;
    }

    public void markAsReturned(List<String> images) {
        if (images == null || images.isEmpty()) {
            throw new ApplicationException(ErrorStatus.RETURN_PHOTO_REQUIRED);
        }
        this.images = images;
        this.status = ReservationStatus.RETURNED;
    }
}

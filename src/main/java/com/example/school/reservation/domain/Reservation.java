package com.example.school.reservation.domain;

import com.example.school.facility.domain.Facility;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.domain.BaseEntity;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
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
import lombok.Builder;
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

    private int headCount;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ElementCollection
    List<String> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Builder
    public Reservation(TimeSlot timeSlot, int headCount, List<String> images, Member member, Facility facility) {
        validateHeadCount(headCount);
        facility.getReservationPolicy().isValidSlot(timeSlot);
        this.timeSlot = timeSlot;
        this.headCount = headCount;
        this.status = ReservationStatus.RESERVED;
        this.images = images;
        this.member = member;
        this.facility = facility;
    }

    private void validateHeadCount(int headCount) {
        if (headCount < 1 || 100000 < headCount) {
            throw new ApplicationException(ErrorStatus.EXPIRED_JWT);
        }
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

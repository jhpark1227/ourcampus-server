package com.example.school.reservation.domain;

import com.example.school.facility.domain.Facility;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.domain.BaseEntity;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TimeSlot timeSlot;

    private int headCount;

    @ElementCollection
    List<String> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    public Reservation(Long id, TimeSlot timeSlot, int headCount, List<String> images, Member member, Facility facility) {
        validateHeadCount(headCount);
        this.id = id;
        this.timeSlot = timeSlot;
        this.headCount = headCount;
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

    public void validateOwner(long memberId) {
        if (member.hasId(memberId)) {
            return;
        }
        throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
    }

    public LocalDateTime calculateScheduledTime(AlarmTiming alarmTiming) {
        return timeSlot.getTimeBefore(alarmTiming.getDuration());
    }
}

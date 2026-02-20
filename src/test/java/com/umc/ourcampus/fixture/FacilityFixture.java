package com.umc.ourcampus.fixture;

import com.umc.ourcampus.facility.domain.MinuteOffset;
import com.umc.ourcampus.facility.domain.Building;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityCategory;
import com.umc.ourcampus.facility.domain.OperationTime;
import com.umc.ourcampus.reservation.domain.AvailableTime;
import com.umc.ourcampus.reservation.domain.ReservationPolicy;
import com.umc.ourcampus.university.domain.University;
import java.util.List;

public class FacilityFixture {

    public static Facility create(Building building) {
        ReservationPolicy policy = new ReservationPolicy(true, List.of(new AvailableTime(new MinuteOffset(540), new MinuteOffset(1200))));
        return Facility.builder()
                .name("시설1")
                .building(building)
                .university(building.getUniversity())
                .category(FacilityCategory.CULTURE)
                .caution("주의사항")
                .location("위치")
                .purpose("목적")
                .equipment("용품")
                .description("설명")
                .operationTimes(List.of(new OperationTime("운영시간1", new MinuteOffset(540), new MinuteOffset(1200))))
                .images(List.of())
                .thumbnailImage("thumbnailImageUrl")
                .reservationPolicy(policy)
                .build();
    }

    public static Facility create(University university) {
        ReservationPolicy policy = createReservablePolicy(9, 20);
        return create(university, policy);
    }

    public static Facility create(University university, ReservationPolicy reservationPolicy) {
        return Facility.builder()
                .name("시설1")
                .university(university)
                .category(FacilityCategory.CULTURE)
                .caution("주의사항")
                .location("위치")
                .purpose("목적")
                .equipment("용품")
                .description("설명")
                .operationTimes(List.of(new OperationTime("운영시간1", new MinuteOffset(540), new MinuteOffset(1200))))
                .images(List.of())
                .thumbnailImage("thumbnailImageUrl")
                .reservationPolicy(reservationPolicy)
                .build();
    }

    public static ReservationPolicy createReservablePolicy(int startTime, int endTIme) {
        return new ReservationPolicy(
                true,
                List.of(new AvailableTime(new MinuteOffset(startTime * 60), new MinuteOffset(endTIme * 60)))
        );
    }

    public static ReservationPolicy createNonReservablePolicy() {
        return new ReservationPolicy(false, List.of());
    }
}

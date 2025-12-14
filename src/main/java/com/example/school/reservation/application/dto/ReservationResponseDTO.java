package com.example.school.reservation.application.dto;

import com.example.school.reservation.domain.AlarmTiming;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationResponseDTO {
    //예약 내역
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailDTO {
        Long id;
        Long facilityId;
        Long memberId;
        Integer users;
        String year;
        String month;
        String day;
        Integer duration;
        Integer start_time;
        Integer end_time;
        Boolean back;
        Set<AlarmTiming> alerts;


    }

    //예약 내역 리스트
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResultDTO {
        List<DetailDTO> resultList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class returnDTO {
        Long id;
        String year;
        String month;
        String day;
        Integer duration;
        Integer start_time;
        Integer end_time;
        Boolean back;
    }

    @Getter
    @AllArgsConstructor
    public static class InUse {
        Long reservationId;
        String facilityName;
        Integer endTime;
        String remainingTime;
    }
}

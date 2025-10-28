package com.example.school.reservation.application.dto;

import com.example.school.user.domain.AlertType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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
        Set<AlertType> alerts;


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

    //예약 가능한 시간
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class bookedUpDTO {
        Long id;
        String year;
        String month;
        String day;
        Integer startTime;
        Integer endTime;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class bookedUpListDTO {
        List<bookedUpDTO> bookedUpList;
        Integer listSize;
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

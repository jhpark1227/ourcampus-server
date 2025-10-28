package com.example.school.reservation.application.dto;

import com.example.school.user.domain.AlertType;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

public class ReservationRequestDTO {
    //예약 내역 확인
    @Getter
    public static class DetailDTO {
        Long memberId;
        Integer page;

    }

    //예약연장
    @Getter
    public static class ExtendDTO {
        Long reservation_id; //예약 아이디
        Integer extendTime; //연장 할 시간
    }

    //예약현황
    @Getter
    public static class StatusDTO {

    }

    //예약기능
    @Getter
    public static class ReservationDTO {
        Long memberId;
        Long facilityId;
        Integer users;
        Integer startTime;
        Integer endTime;
        Integer duration;
        String year;
        String month;
        String day;
        Set<AlertType> alerts;

        public void setAlerts(Set<String> alerts) {
            this.alerts = alerts.stream().map(alert -> {
                try {
                    return AlertType.valueOf(alert.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid alert type: " + alert);
                }
            }).collect(Collectors.toSet());
        }
    }

    //예약 불가능한 시간대
    @Getter
    public static class bookedUpDTO {
        Long facilityId;
        String year;
        String month;
        String day;
    }

    //반납하기
    @Getter
    public static class returnDTO {
        Long reservationId;
        boolean checkList1;
        boolean checkList2;
        boolean checkList3;
        boolean checkList4;
    }

    //반납 사진 받기
    @Getter
    public static class returnImg {

    }

}

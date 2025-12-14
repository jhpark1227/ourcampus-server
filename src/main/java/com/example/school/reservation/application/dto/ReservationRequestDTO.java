package com.example.school.reservation.application.dto;

import lombok.Getter;

public class ReservationRequestDTO {

    //예약연장
    @Getter
    public static class ExtendDTO {
        Long reservation_id; //예약 아이디
        Integer extendTime; //연장 할 시간
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
}

package com.example.school.reservation.converter;

import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.application.dto.ReservationRequestDTO;
import com.example.school.reservation.application.dto.ReservationResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationConverter {
    //예약 세부내용
    public static ReservationResponseDTO.DetailDTO detailResultDTO(Reservation reservation) {
        return ReservationResponseDTO.DetailDTO.builder()
                .id(reservation.getId())
                .facilityId(reservation.getFacility().getId())
                .memberId(reservation.getMember().getId())
                .users(reservation.getUsers())
                .year(reservation.getYear())
                .month(reservation.getMonth())
                .start_time(reservation.getStart_time())
                .end_time(reservation.getEnd_time())
                .day(reservation.getDay())
                .duration(reservation.getDuration())
                .back(reservation.getBack())
                .alerts(reservation.getAlerts())
                .build();
    }

    //예약 세부 내용들 list
    public static ReservationResponseDTO.DetailResultDTO detailResultListDTO(Page<Reservation> reservationList) {
        List<ReservationResponseDTO.DetailDTO> reservationDTO = reservationList.stream()
                .map(ReservationConverter::detailResultDTO).collect(Collectors.toList());
        return ReservationResponseDTO.DetailResultDTO.builder()
                .resultList(reservationDTO)
                .isFirst(reservationList.isFirst())
                .isLast(reservationList.isLast())
                .totalPage(reservationList.getTotalPages())
                .totalElements(reservationList.getTotalElements())
                .listSize(reservationDTO.size())
                .build();
    }

    //예약 불가능한 시간대
    public static ReservationResponseDTO.bookedUpDTO bookedUpDTO(Reservation reservation) {
        return ReservationResponseDTO.bookedUpDTO.builder()
                .id(reservation.getId())
                .endTime(reservation.getEnd_time())
                .startTime(reservation.getStart_time())
                .year(reservation.getYear())
                .month(reservation.getMonth())
                .day(reservation.getDay())
                .build();
    }

    //예약 불가능한 시간대 세부내용 리스트
    public static ReservationResponseDTO.bookedUpListDTO bookedUpListDTO(List<Reservation> reservationList) {
        List<ReservationResponseDTO.bookedUpDTO> reservationDTO = reservationList.stream()
                .map(ReservationConverter::bookedUpDTO).collect(Collectors.toList());
        return ReservationResponseDTO.bookedUpListDTO.builder()
                .bookedUpList(reservationDTO)
                .listSize(reservationList.size())
                .build();
    }

    //예약 하기 -> 예약 만듬
    public static Reservation reservation(ReservationRequestDTO.ReservationDTO reservationDTO) {
        return Reservation.builder()
                .year(reservationDTO.getYear())
                .users(reservationDTO.getUsers())
                .month(reservationDTO.getMonth())
                .day(reservationDTO.getDay())
                .start_time(reservationDTO.getStartTime())
                .end_time(reservationDTO.getEndTime())
                .duration(reservationDTO.getDuration())
                .alerts(reservationDTO.getAlerts())
                .back(false)
                .build();
    }

    //예약 한 시설물 반납하기
    public static ReservationResponseDTO.returnDTO returnReservation(Reservation reservation) {
        return ReservationResponseDTO.returnDTO.builder()
                .id(reservation.getId())
                .year(reservation.getYear())
                .month(reservation.getMonth())
                .start_time(reservation.getStart_time())
                .end_time(reservation.getEnd_time())
                .day(reservation.getDay())
                .duration(reservation.getDuration())
                .back(reservation.getBack())
                .build();
    }

}

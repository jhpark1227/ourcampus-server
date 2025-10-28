package com.example.school.facility.converter;

import com.example.school.facility.domain.Facility;
import com.example.school.reservation.domain.Reservation;
import com.example.school.facility.application.dto.FacilityResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class FacilityConverter {
    public static FacilityResponseDTO.DetailDTO detailDTO(Facility facility, Reservation reservation) {
        return FacilityResponseDTO.DetailDTO.builder()
                .facilityId(facility.getId())
                .name(facility.getName())
                .imageURL(facility.getImageURL())
                .time(facility.getTime())
                .location(facility.getLocation())
                .score(facility.getScore())
                .purpose(facility.getPurpose())
                .year(reservation.getYear())
                .month(reservation.getMonth())
                .day(reservation.getDay())
                .startTime(reservation.getStart_time())
                .endTime(reservation.getEnd_time())
                .duration(reservation.getDuration())
                .build();
    }

    public static FacilityResponseDTO.DetailResultDTO detailResultDTO(Page<Facility> facilityList,
                                                                      Page<Reservation> reservationList) {
        List<FacilityResponseDTO.DetailDTO> facilityDTO = reservationList.stream()
                .map((Reservation reservation) -> {
                    Facility facility = reservation.getFacility();
                    return detailDTO(facility, reservation);
                })
                .collect(Collectors.toList());
        return FacilityResponseDTO.DetailResultDTO.builder()
                .resultList(facilityDTO)
                .isFirst(reservationList.isFirst())
                .isLast(reservationList.isLast())
                .totalPage(reservationList.getTotalPages())
                .listSize(reservationList.getSize())
                .totalElements(reservationList.getTotalElements())
                .build();
    }
}

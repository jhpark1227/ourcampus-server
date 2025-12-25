package com.example.school.facility.application;

import com.example.school.facility.application.dto.FacilityResponseDTO;
import com.example.school.facility.application.dto.response.FacilityResponse;
import com.example.school.facility.application.dto.response.FacilityScheduleResponse;
import com.example.school.facility.domain.Building;
import com.example.school.facility.domain.BuildingRepository;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.facility.domain.FacilityTheme;
import com.example.school.facility.domain.FacilityThemeRepository;
import com.example.school.facility.domain.Theme;
import com.example.school.facility.domain.ThemeRepository;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.reservation.application.dto.response.TimeSlotWithBookedResponse;
import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.domain.ReservationRepository;
import com.example.school.reservation.domain.TimeSlot;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final ThemeRepository themeRepository;
    private final FacilityThemeRepository facilityThemeRepository;
    private final BuildingRepository buildingRepository;
    private final ReservationRepository reservationRepository;
    private final RedisTemplate redisTemplate;

    public List<TimeSlotWithBookedResponse> getTimesByFacilityAndDate(long facilityId, LocalDate date) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        List<Reservation> reservations = reservationRepository.findByFacilityAndDate(facility, date);

        List<TimeSlot> timeSlots = facility.getTimeSlots(date);
        Collection<TimeSlot> availableTimeSlots = facility.getAvailableTimeSlots(date, reservations);
        return timeSlots.stream()
                .map(timeSlot -> TimeSlotWithBookedResponse.from(timeSlot, !availableTimeSlots.contains(timeSlot)))
                .toList();
    }

    public List<FacilityResponse> findFacilitiesByBuildingId(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.BUILDING_NOT_FOUND));

        return facilityRepository.findByBuilding(building)
                .stream()
                .map(FacilityResponse::from)
                .toList();
    }

    public List<FacilityResponse> findFacilitiesByThemeId(Long themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.THEME_NOT_FOUND));
        List<FacilityTheme> facilityThemes = facilityThemeRepository.findByTheme(theme);

        return facilityThemes
                .stream()
                .map(FacilityTheme::getFacility)
                .map(FacilityResponse::from)
                .toList();
    }

    public void saveSearchLog(Long memberId, Long schoolId, String value) {
        String key = searchLogKeyBySchool(schoolId);
        redisTemplate.opsForList().rightPush(key, value);

        key = searchLogKey(memberId);
        Long size = redisTemplate.opsForZSet().size(key);
        if (size == 10) {
            redisTemplate.opsForZSet().removeRange(key, 0, 0);
        }
        redisTemplate.opsForZSet().add(key, value, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public String searchLogKey(Long memberId) {
        return "SearchLog:" + memberId;
    }

    public String searchLogKeyBySchool(Long schoolId) {
        return "School:" + schoolId;
    }

    public FacilityResponseDTO.DeleteSearchLog deleteSearchLog(Long memberId, String value) {
        String key = searchLogKey(memberId);

        long count = redisTemplate.opsForZSet().remove(key, value);

        if (count != 1) {
            throw new GeneralException(ErrorStatus.BAD_REQUEST);
        }
        return new FacilityResponseDTO.DeleteSearchLog(value);
    }

    public List<FacilityScheduleResponse> getWeeklySchedule(Long facilityId, LocalDate baseDate) {
        List<FacilityScheduleResponse> facilitySchedules = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = baseDate.plusDays(i);
            List<TimeSlotWithBookedResponse> times = getTimesByFacilityAndDate(facilityId, date);
            facilitySchedules.add(new FacilityScheduleResponse(times, date));
        }
        return facilitySchedules;
    }
}

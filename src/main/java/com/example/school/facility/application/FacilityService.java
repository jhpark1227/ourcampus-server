package com.example.school.facility.application;

import com.example.school.facility.application.dto.response.FacilityResponse;
import com.example.school.facility.application.dto.response.FacilityScheduleResponse;
import com.example.school.facility.domain.Building;
import com.example.school.facility.domain.BuildingRepository;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityCategory;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.facility.domain.FacilityTheme;
import com.example.school.facility.domain.FacilityThemeRepository;
import com.example.school.facility.domain.Theme;
import com.example.school.facility.domain.ThemeRepository;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.reservation.application.dto.response.TimeSlotWithBookedResponse;
import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.domain.ReservationRepository;
import com.example.school.reservation.domain.TimeSlot;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    private final UniversityRepository universityRepository;

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

    public List<FacilityResponse> findFacilitiesByBuildingId(long buildingId) {
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

    public List<FacilityScheduleResponse> getWeeklySchedule(Long facilityId, LocalDate baseDate) {
        List<FacilityScheduleResponse> facilitySchedules = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = baseDate.plusDays(i);
            List<TimeSlotWithBookedResponse> times = getTimesByFacilityAndDate(facilityId, date);
            facilitySchedules.add(new FacilityScheduleResponse(times, date));
        }
        return facilitySchedules;
    }

    public FacilityResponse findFacilityById(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));

        return FacilityResponse.from(facility);
    }

    public List<FacilityResponse> findFacilityByUniversityAndCategory(Long universityId, FacilityCategory category) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        List<Facility> facilities = facilityRepository.findByUniversityAndCategory(university, category);
        return facilities.stream()
                .map(FacilityResponse::from)
                .toList();
    }

    public List<FacilityResponse> searchFacilitiesByKeyword(String keyword, long universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        return facilityRepository.findByNameLikeAndUniversity(keyword, university)
                .stream()
                .map(FacilityResponse::from)
                .toList();
    }
}

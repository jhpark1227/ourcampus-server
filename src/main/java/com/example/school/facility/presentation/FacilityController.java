package com.example.school.facility.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.facility.application.FacilityService;
import com.example.school.facility.application.dto.response.FacilityResponse;
import com.example.school.facility.application.dto.response.FacilityScheduleResponse;
import com.example.school.facility.domain.FacilityCategory;
import com.example.school.reservation.application.dto.response.TimeSlotWithBookedResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping("/facilities/{facilityId}")
    public FacilityResponse getFacility(@PathVariable("facilityId") long id) {
        return facilityService.findFacilityById(id);
    }

    @GetMapping("/facilities")
    public List<FacilityResponse> getFacilities(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestParam("category") FacilityCategory category
    ) {
        return facilityService.findFacilityByUniversityAndCategory(memberPrincipal.universityId(), category);
    }

    @GetMapping("/buildings/{buildingId}/facilities")
    public List<FacilityResponse> getFacilitiesByBuilding(@PathVariable("buildingId") long buildingId) {
        return facilityService.findFacilitiesByBuildingId(buildingId);
    }

    @GetMapping("/themes/{themeId}/facilities")
    public List<FacilityResponse> getFacilitiesByTheme(@PathVariable("themeId") long themeId) {
        return facilityService.findFacilitiesByThemeId(themeId);
    }

    @GetMapping("/facilities/{facilityId}/times")
    public List<TimeSlotWithBookedResponse> getAvailableTime(
            @PathVariable("facilityId") long facilityId, @RequestParam LocalDate date
    ) {
        return facilityService.getTimesByFacilityAndDate(facilityId, date);
    }

    @GetMapping("/facilities/{facilityId}/weekly-schedule")
    public List<FacilityScheduleResponse> getWeeklySchedule(
            @PathVariable(name = "facilityId") Long facilityId,
            @RequestParam(name = "date") LocalDate date
    ) {
        return facilityService.getWeeklySchedule(facilityId, date);
    }

    @GetMapping("/facilities/search")
    public List<FacilityResponse> searchFacilities(
            @RequestParam("keyword") String keyword,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        return facilityService.searchFacilitiesByKeyword(keyword, memberPrincipal.universityId());
    }
}

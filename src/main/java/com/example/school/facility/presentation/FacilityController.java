package com.example.school.facility.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.facility.application.FacilityQueryService;
import com.example.school.facility.application.FacilityService;
import com.example.school.facility.application.LibraryService;
import com.example.school.facility.application.dto.FacilityResponseDTO;
import com.example.school.facility.application.dto.response.FacilityResponse;
import com.example.school.facility.application.dto.response.FacilityScheduleResponse;
import com.example.school.facility.domain.FacilityCategory;
import com.example.school.global.apiPayload.ApiResponse;
import com.example.school.member.domain.Member;
import com.example.school.reservation.application.dto.response.TimeSlotWithBookedResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;
    private final FacilityQueryService facilityQueryService;
    private final LibraryService libraryService;

    @GetMapping("/facilities/{facilityId}")
    public FacilityResponse getFacility(@PathVariable("facilityId") long id) {
        return facilityQueryService.findFacilityById(id);
    }

    @GetMapping("/facilities")
    public List<FacilityResponse> getFacilities(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestParam("category") FacilityCategory category
    ) {
        return facilityQueryService.findFacilityByUniversityAndCategory(memberPrincipal.universityId(), category);
    }

    @GetMapping("/me/buildings/{buildingId}/facilities")
    public List<FacilityResponse> getFacilitiesByBuilding(@PathVariable("buildingId") long buildingId) {
        return facilityService.findFacilitiesByBuildingId(buildingId);
    }

    @GetMapping("/me/themes/{themeId}/facilities")
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

    @GetMapping("/keyword/{keyword}")
    public ApiResponse<FacilityResponseDTO.ListByKeyword> getListByKeyword(
            @PathVariable("keyword") String keyword, Authentication auth) {
        Member member = (Member) auth.getPrincipal();

        FacilityResponseDTO.ListByKeyword res = facilityQueryService.getListByKeyword(member.getId(), keyword);

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/search")
    public ApiResponse<FacilityResponseDTO.SearchResults> searchFacility(
            @RequestParam("query") String keyword,
            @RequestParam("page") Integer page,
            Authentication auth
    ) {
        Member member = (Member) auth.getPrincipal();

        FacilityResponseDTO.SearchResults res = facilityQueryService.searchFacility(member.getId(), keyword, page);

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/library")
    public ApiResponse<Object> getLibraryStatus(Authentication auth) {
        Member member = (Member) auth.getPrincipal();

        FacilityResponseDTO.LibraryStatus res = libraryService.getLibraryStatus(member.getId());

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/search-log")
    public ApiResponse<FacilityResponseDTO.SearchLogList> getSearchLog(Authentication auth) {
        Member member = (Member) auth.getPrincipal();

        FacilityResponseDTO.SearchLogList res = facilityQueryService.getSearchLog(member.getId());

        return ApiResponse.onSuccess(res);
    }

    @DeleteMapping("/search-log/{value}")
    public ApiResponse<FacilityResponseDTO.DeleteSearchLog> deleteSearchLog(
            @PathVariable(name = "value") String value, Authentication auth
    ) {
        Member member = (Member) auth.getPrincipal();

        FacilityResponseDTO.DeleteSearchLog res = facilityService.deleteSearchLog(member.getId(), value);

        return ApiResponse.onSuccess(res);
    }
}

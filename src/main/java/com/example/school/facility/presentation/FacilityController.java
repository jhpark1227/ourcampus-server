package com.example.school.facility.presentation;

import com.example.school.facility.application.FacilityQueryService;
import com.example.school.facility.application.FacilityService;
import com.example.school.facility.application.LibraryService;
import com.example.school.facility.application.dto.FacilityResponseDTO;
import com.example.school.global.apiPayload.ApiResponse;
import com.example.school.global.validation.annotation.CheckKeyword;
import com.example.school.global.validation.annotation.CheckPage;
import com.example.school.global.validation.annotation.ExistKeyword;
import com.example.school.user.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/facility")
@RequiredArgsConstructor
public class FacilityController {
    private final FacilityService facilityService;
    private final FacilityQueryService facilityQueryService;
    private final LibraryService libraryService;

    @GetMapping("/category/theme")
    public ApiResponse<FacilityResponseDTO.ListByTheme> getListByTheme(Authentication auth) {
        Member member = (Member) auth.getPrincipal();

        FacilityResponseDTO.ListByTheme res = facilityService.getListByTheme(member.getId());

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/category/building")
    public ApiResponse<FacilityResponseDTO.ListByBuilding> getListByBuilding(Authentication auth) {
        Member member = (Member) auth.getPrincipal();
        FacilityResponseDTO.ListByBuilding res = facilityService.getListByBuilding(member.getId());

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/map")
    public ApiResponse<FacilityResponseDTO.Markers> getMarkers(Authentication auth) {
        Member member = (Member) auth.getPrincipal();

        FacilityResponseDTO.Markers res = facilityService.getMarkers(member.getId());

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/suggestion")
    public ApiResponse<FacilityResponseDTO.Tags> getSuggestion(Authentication auth) {
        Member member = (Member) auth.getPrincipal();

        FacilityResponseDTO.Tags res = facilityService.getSuggestion(member.getId());

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/{facilityId}")
    public ApiResponse<FacilityResponseDTO.Detail> getDetail(@PathVariable("facilityId") Long facilityId) {
        FacilityResponseDTO.Detail res = facilityQueryService.getDetail(facilityId);

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/building/{buildingId}")
    public ApiResponse<FacilityResponseDTO.BuildingDetail> getBuildingDetail(
            @PathVariable("buildingId") Long buildingId) {
        FacilityResponseDTO.BuildingDetail res = facilityQueryService.getBuildingDetail(buildingId);

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/building/{buildingId}/img")
    public ApiResponse<FacilityResponseDTO.BuildingImages> getBuildingImages(
            @PathVariable("buildingId") Long facilityId,
            @RequestParam("page") @CheckPage Integer page
    ) {
        FacilityResponseDTO.BuildingImages res = facilityQueryService.getBuildingImages(facilityId, page);

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/{facilityId}/img")
    public ApiResponse<FacilityResponseDTO.Images> getImages(
            @PathVariable("facilityId") Long facilityId,
            @RequestParam("page") @CheckPage Integer page
    ) {
        FacilityResponseDTO.Images res = facilityQueryService.getImages(facilityId, page);

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/keyword/{keyword}")
    public ApiResponse<FacilityResponseDTO.ListByKeyword> getListByKeyword(
            @PathVariable("keyword") @ExistKeyword String keyword, Authentication auth) {
        Member member = (Member) auth.getPrincipal();

        FacilityResponseDTO.ListByKeyword res = facilityQueryService.getListByKeyword(member.getId(), keyword);

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/map/{buildingId}")
    public ApiResponse<FacilityResponseDTO.DetailOnMarker> getDetailOnMarker(
            @PathVariable("buildingId") Long buildingId) {
        FacilityResponseDTO.DetailOnMarker res = facilityQueryService.getDetailOnMarker(buildingId);

        return ApiResponse.onSuccess(res);
    }

    @GetMapping("/search")
    public ApiResponse<FacilityResponseDTO.SearchResults> searchFacility(
            @RequestParam("query") @CheckKeyword String keyword,
            @RequestParam("page") @CheckPage Integer page,
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

    @GetMapping("/search-rank")
    public ApiResponse<FacilityResponseDTO.SearchRankList> getSearchRank(Authentication auth) {
        Member member = (Member) auth.getPrincipal();

        FacilityResponseDTO.SearchRankList res = facilityQueryService.getSearchRank(member.getId());

        return ApiResponse.onSuccess(res);
    }
}

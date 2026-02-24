package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.FacilityService;
import com.umc.ourcampus.facility.application.dto.request.AddFacilityToThemeRequest;
import com.umc.ourcampus.facility.application.dto.request.AssignBuildingRequest;
import com.umc.ourcampus.facility.application.dto.response.FacilityDetailResponse;
import com.umc.ourcampus.facility.application.dto.response.FacilityResponse;
import com.umc.ourcampus.facility.domain.FacilityCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @PostMapping("/admin/themes/{themeId}/facilities")
    public void addFacilityToTheme(
            @PathVariable("themeId") long themeId,
            @RequestBody AddFacilityToThemeRequest request
    ) {
        facilityService.addFacilityToTheme(themeId, request);
    }

    @GetMapping("/facilities/{facilityId}")
    public FacilityDetailResponse getFacilityDetail(@PathVariable("facilityId") long id) {
        return facilityService.getFacilityDetail(id);
    }

    @GetMapping("/facilities")
    public List<FacilityResponse> getFacilities(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("category") FacilityCategory category
    ) {
        return facilityService.findByUniversityAndCategory(userPrincipal.universityId(), category);
    }

    @GetMapping("/universities/{universityId}/facilities")
    public List<FacilityResponse> getFacilities(@PathVariable("universityId") long universityId) {
        return facilityService.getFacilities(universityId);
    }

    @GetMapping("/buildings/{buildingId}/facilities")
    public List<FacilityResponse> getFacilitiesByBuilding(@PathVariable("buildingId") long buildingId) {
        return facilityService.findFacilitiesByBuilding(buildingId);
    }

    @GetMapping("/themes/{themeId}/facilities")
    public List<FacilityResponse> getFacilitiesByTheme(@PathVariable("themeId") long themeId) {
        return facilityService.findFacilitiesByTheme(themeId);
    }

    @GetMapping("/facilities/search")
    public List<FacilityResponse> search(
            @RequestParam("keyword") String keyword,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return facilityService.search(keyword, userPrincipal.universityId());
    }

    @PatchMapping("/admin/facilities/{facilityId}/building")
    public void assignBuilding(
            @PathVariable("facilityId") long facilityId,
            @RequestBody AssignBuildingRequest request
    ) {
        facilityService.addFacilityToBuilding(facilityId, request);
    }
}

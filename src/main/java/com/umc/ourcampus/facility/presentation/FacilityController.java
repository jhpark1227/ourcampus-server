package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.auth.domain.MemberPrincipal;
import com.umc.ourcampus.facility.application.FacilityService;
import com.umc.ourcampus.facility.application.dto.response.FacilityDetailResponse;
import com.umc.ourcampus.facility.application.dto.response.FacilityResponse;
import com.umc.ourcampus.facility.domain.FacilityCategory;
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
    public FacilityDetailResponse getFacilityDetail(@PathVariable("facilityId") long id) {
        return facilityService.getFacilityDetail(id);
    }

    @GetMapping("/facilities")
    public List<FacilityResponse> getFacilities(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestParam("category") FacilityCategory category
    ) {
        return facilityService.findByUniversityAndCategory(memberPrincipal.universityId(), category);
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
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        return facilityService.search(keyword, memberPrincipal.universityId());
    }
}

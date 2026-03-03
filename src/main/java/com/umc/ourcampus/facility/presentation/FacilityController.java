package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.FacilityService;
import com.umc.ourcampus.facility.application.dto.request.UpdateFacilityRequest;
import com.umc.ourcampus.facility.application.dto.response.FacilityDetailResponse;
import com.umc.ourcampus.facility.application.dto.response.FacilityResponse;
import com.umc.ourcampus.facility.domain.FacilityCategory;
import com.umc.ourcampus.facility.presentation.dto.request.CreateFacilityWebRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @PostMapping("/admin/universities/{universityId}/facilities")
    public void createFacility(
            @PathVariable("universityId") long universityId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody CreateFacilityWebRequest request
    ) {

        facilityService.createFacility(universityId, principal, request.toDto());
    }

    @GetMapping("/facilities/{facilityId}")
    public FacilityDetailResponse getFacilityDetail(@PathVariable("facilityId") long id) {
        return facilityService.getFacilityDetail(id);
    }

    @GetMapping("/universities/{universityId}/facilities")
    public List<FacilityResponse> getFacilities(
            @PathVariable("universityId") long universityId,
            @RequestParam(value = "category", required = false) FacilityCategory category
    ) {
        return facilityService.getFacilities(universityId, category);
    }

    @GetMapping("/buildings/{buildingId}/facilities")
    public List<FacilityResponse> getFacilitiesByBuilding(@PathVariable("buildingId") long buildingId) {
        return facilityService.findFacilitiesByBuilding(buildingId);
    }

    @GetMapping("/themes/{themeId}/facilities")
    public List<FacilityResponse> getFacilitiesByTheme(@PathVariable("themeId") long themeId) {
        return facilityService.findFacilitiesByTheme(themeId);
    }

    @GetMapping("/universities/{universityId}/facilities/search")
    public List<FacilityResponse> search(
            @PathVariable("universityId") long universityId,
            @RequestParam("keyword") String keyword
    ) {
        return facilityService.search(keyword, universityId);
    }

    @PutMapping("/admin/facilities/{facilityId}")
    public ResponseEntity<Void> updateFacility(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("facilityId") long facilityId,
            @RequestBody UpdateFacilityRequest request
    ) {
        facilityService.updateFacility(principal, facilityId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/facilities/{facilityId}")
    public ResponseEntity<Void> deleteFacility(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("facilityId") long facilityId
    ) {
        facilityService.deleteFacility(principal, facilityId);
        return ResponseEntity.noContent().build();
    }
}

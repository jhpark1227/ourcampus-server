package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.BuildingService;
import com.umc.ourcampus.facility.application.dto.request.BuildingCreateRequest;
import com.umc.ourcampus.facility.application.dto.response.BuildingResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @PostMapping("/admin/universities/{universityId}/buildings")
    public void createBuilding(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("universityId") long universityId,
            @RequestBody BuildingCreateRequest request
    ) {
        buildingService.createBuilding(principal, universityId, request);
    }

    @GetMapping("/buildings/{buildingId}")
    public BuildingResponse getBuilding(@PathVariable("buildingId") Long id) {
        return buildingService.findBuildingById(id);
    }

    @GetMapping("/universities/{universityId}/buildings")
    public List<BuildingResponse> getBuildings(@PathVariable("universityId") long universityId) {
        return buildingService.findBuildingsByUniversityId(universityId);
    }
}

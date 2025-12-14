package com.example.school.facility.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.facility.application.BuildingService;
import com.example.school.facility.application.dto.response.BuildingResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @GetMapping("/buildings/{buildingId}")
    public BuildingResponse getBuilding(@PathVariable("buildingId") Long id) {
        return buildingService.findBuildingById(id);
    }

    @GetMapping("/me/buildings")
    public List<BuildingResponse> getBuildings(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return buildingService.findBuildingsByUniversityId(memberPrincipal.universityId());
    }
}

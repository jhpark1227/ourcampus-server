package com.umc.ourcampus.facility.application;

import com.umc.ourcampus.facility.application.dto.response.BuildingResponse;
import com.umc.ourcampus.facility.domain.Building;
import com.umc.ourcampus.facility.domain.BuildingRepository;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final UniversityRepository universityRepository;

    public List<BuildingResponse> findBuildingsByUniversityId(Long universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        List<Building> buildings = buildingRepository.findAllByUniversity(university);

        return buildings.stream().map(BuildingResponse::from).toList();
    }

    public BuildingResponse findBuildingById(Long id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.BUILDING_NOT_FOUND));

        return BuildingResponse.from(building);
    }
}

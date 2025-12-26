package com.example.school.facility.application;

import com.example.school.facility.application.dto.response.BuildingResponse;
import com.example.school.facility.domain.Building;
import com.example.school.facility.domain.BuildingRepository;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
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

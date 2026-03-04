package com.umc.ourcampus.facility.application;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.dto.request.BuildingCreateRequest;
import com.umc.ourcampus.facility.application.dto.request.UpdateBuildingRequest;
import com.umc.ourcampus.facility.application.dto.response.BuildingResponse;
import com.umc.ourcampus.facility.domain.Building;
import com.umc.ourcampus.facility.domain.BuildingRepository;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.file.application.FileManager;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final FacilityRepository facilityRepository;
    private final UniversityRepository universityRepository;
    private final FileManager fileManager;

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

    public void createBuilding(UserPrincipal principal, long universityId, BuildingCreateRequest request) {
        if (principal.universityId() != universityId) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        validateImages(Stream.concat(Stream.of(request.thumbnailImage()), request.images().stream()).toList());
        Building building = Building.create(
                request.name(),
                request.latitude(),
                request.longitude(),
                request.label(),
                request.thumbnailImage(),
                request.images(),
                request.toOperationTimes(),
                university
        );
        buildingRepository.save(building);
    }

    public void deleteBuilding(UserPrincipal principal, long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.BUILDING_NOT_FOUND));
        if (!building.getUniversity().equalId(principal.universityId())) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        facilityRepository.findByBuilding(building)
                .forEach(Facility::clearBuilding);
        buildingRepository.delete(building);
    }

    public void updateBuilding(UserPrincipal principal, long buildingId, UpdateBuildingRequest request) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.BUILDING_NOT_FOUND));
        if (!building.getUniversity().equalId(principal.universityId())) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        validateImages(Stream.concat(Stream.of(request.thumbnailImage()), request.images().stream()).toList());
        building.update(
                request.name(),
                request.latitude(),
                request.longitude(),
                request.label(),
                request.thumbnailImage(),
                request.images(),
                request.toOperationTimes()
        );
    }

    private void validateImages(List<String> images) {
        if (images.stream().anyMatch(imageUrl -> !fileManager.exist(imageUrl))) {
            throw new ApplicationException(ErrorStatus.IMAGE_NOT_FOUND);
        }
    }
}

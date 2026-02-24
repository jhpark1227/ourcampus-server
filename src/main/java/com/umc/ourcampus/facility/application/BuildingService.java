package com.umc.ourcampus.facility.application;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.dto.request.BuildingCreateRequest;
import com.umc.ourcampus.facility.application.dto.response.BuildingResponse;
import com.umc.ourcampus.facility.domain.Building;
import com.umc.ourcampus.facility.domain.BuildingRepository;
import com.umc.ourcampus.facility.domain.MinuteOffset;
import com.umc.ourcampus.facility.domain.OperationTime;
import com.umc.ourcampus.file.application.FileManager;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
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
        validateImages(List.of(request.thumbnailImage()));
        validateImages(request.images());
        Building building = Building.create(
                request.name(),
                request.latitude(),
                request.longitude(),
                request.label(),
                request.thumbnailImage(),
                request.images(),
                request.operationTimes()
                        .stream()
                        .map(o -> new OperationTime(
                                o.name(),
                                new MinuteOffset(o.startTime().getHour() * 60 + o.startTime().getMinute()),
                                new MinuteOffset(o.endTime().getHour() * 60 + o.endTime().getMinute())
                        ))
                        .toList(),
                university
        );
        buildingRepository.save(building);
    }

    private void validateImages(List<String> images) {
        if (images.stream().anyMatch(imageUrl -> !fileManager.exist(imageUrl))) {
            throw new ApplicationException(ErrorStatus.IMAGE_NOT_FOUND);
        }
    }
}

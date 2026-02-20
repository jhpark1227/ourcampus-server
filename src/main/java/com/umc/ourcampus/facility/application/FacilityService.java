package com.umc.ourcampus.facility.application;

import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.facility.domain.FacilityThemeRepository;
import com.umc.ourcampus.university.domain.UniversityRepository;
import com.umc.ourcampus.facility.application.dto.response.FacilityDetailResponse;
import com.umc.ourcampus.facility.application.dto.response.FacilityResponse;
import com.umc.ourcampus.facility.domain.Building;
import com.umc.ourcampus.facility.domain.BuildingRepository;
import com.umc.ourcampus.facility.domain.FacilityCategory;
import com.umc.ourcampus.facility.domain.FacilityTheme;
import com.umc.ourcampus.facility.domain.Theme;
import com.umc.ourcampus.facility.domain.ThemeRepository;
import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.review.domain.ReviewRepository;
import com.umc.ourcampus.university.domain.University;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final ThemeRepository themeRepository;
    private final FacilityThemeRepository facilityThemeRepository;
    private final BuildingRepository buildingRepository;
    private final UniversityRepository universityRepository;
    private final ReviewRepository reviewRepository;

    public List<FacilityResponse> findFacilitiesByBuilding(long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.BUILDING_NOT_FOUND));

        return facilityRepository.findByBuilding(building)
                .stream()
                .map(FacilityResponse::from)
                .toList();
    }

    public List<FacilityResponse> findFacilitiesByTheme(Long themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.THEME_NOT_FOUND));
        List<FacilityTheme> facilityThemes = facilityThemeRepository.findByTheme(theme);

        return facilityThemes
                .stream()
                .map(FacilityTheme::getFacility)
                .map(FacilityResponse::from)
                .toList();
    }

    public FacilityDetailResponse getFacilityDetail(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        double averageStarRating = reviewRepository.findAverageStarRatingByFacility(facility);

        return FacilityDetailResponse.of(facility, averageStarRating);
    }

    public List<FacilityResponse> findByUniversityAndCategory(Long universityId, FacilityCategory category) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        List<Facility> facilities = facilityRepository.findByUniversityAndCategory(university, category);
        return facilities.stream()
                .map(FacilityResponse::from)
                .toList();
    }

    public List<FacilityResponse> search(String keyword, long universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        return facilityRepository.findByNameLikeAndUniversity(keyword, university)
                .stream()
                .map(FacilityResponse::from)
                .toList();
    }
}

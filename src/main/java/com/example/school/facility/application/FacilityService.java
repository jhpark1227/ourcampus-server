package com.example.school.facility.application;

import com.example.school.facility.application.dto.response.FacilityDetailResponse;
import com.example.school.facility.application.dto.response.FacilityResponse;
import com.example.school.facility.domain.Building;
import com.example.school.facility.domain.BuildingRepository;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityCategory;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.facility.domain.FacilityTheme;
import com.example.school.facility.domain.FacilityThemeRepository;
import com.example.school.facility.domain.Theme;
import com.example.school.facility.domain.ThemeRepository;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.review.domain.ReviewRepository;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
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

package com.umc.ourcampus.facility.application;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.dto.request.CreateFacilityRequest;
import com.umc.ourcampus.facility.application.dto.request.UpdateFacilityRequest;
import com.umc.ourcampus.facility.application.dto.response.FacilityDetailResponse;
import com.umc.ourcampus.facility.application.dto.response.FacilityResponse;
import com.umc.ourcampus.facility.domain.Building;
import com.umc.ourcampus.facility.domain.BuildingRepository;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityCategory;
import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.facility.domain.FacilityTheme;
import com.umc.ourcampus.facility.domain.FacilityThemeRepository;
import com.umc.ourcampus.facility.domain.Theme;
import com.umc.ourcampus.facility.domain.ThemeRepository;
import com.umc.ourcampus.file.application.FileManager;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.reservation.domain.ReservationRepository;
import com.umc.ourcampus.review.domain.ReviewRepository;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
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
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final FileManager fileManager;

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
        List<Theme> themes = facilityThemeRepository.findByFacility(facility)
                .stream()
                .map(FacilityTheme::getTheme)
                .toList();
        double averageStarRating = reviewRepository.findAverageStarRatingByFacility(facility);

        return FacilityDetailResponse.of(facility, averageStarRating, themes);
    }

    public List<FacilityResponse> getFacilities(Long universityId, FacilityCategory category) {
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

    public void updateFacility(UserPrincipal principal, long facilityId, UpdateFacilityRequest request) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        if (facility.getUniversity().getId() != principal.universityId()) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        Building building = null;
        if (request.buildingId() != null) {
            building = buildingRepository.findById(request.buildingId())
                    .orElseThrow(() -> new ApplicationException(ErrorStatus.BUILDING_NOT_FOUND));
        }
        validateImages(request.images());
        validateImages(List.of(request.thumbnailImage()));
        facility.update(request.name(), request.description(), request.purpose(), request.equipment(), request.caution(),
                request.location(), request.thumbnailImage(), request.category(), request.toReservationPolicy(), request.toOperationTimes(),
                request.images(), building);

        facilityThemeRepository.deleteByFacility(facility);
        List<FacilityTheme> facilityThemes = themeRepository.findAllById(request.themeIds())
                .stream()
                .map(theme -> new FacilityTheme(facility, theme))
                .toList();
        facilityThemeRepository.saveAll(facilityThemes);
    }

    public void createFacility(long universityId, UserPrincipal principal, CreateFacilityRequest request) {
        if (principal.universityId() != universityId) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        Building building = buildingRepository.findById(request.buildingId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.BUILDING_NOT_FOUND));
        validateImages(request.images());
        Facility facility = new Facility(
                request.name(),
                request.description(),
                request.purpose(),
                request.equipment(),
                request.caution(),
                request.location(),
                request.thumbnailImage(),
                request.category(),
                request.toReservationPolicy(),
                request.toOperationTimes(),
                request.images(),
                building,
                university
        );
        facilityRepository.save(facility);
    }

    public void deleteFacility(UserPrincipal principal, long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        if (!facility.getUniversity().equalId(principal.universityId())) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        reviewRepository.deleteByReservation_Facility(facility);
        reservationRepository.deleteByFacility(facility);
        facilityThemeRepository.deleteByFacility(facility);
        facilityRepository.delete(facility);
    }

    private void validateImages(List<String> images) {
        if (images.stream().anyMatch(imageUrl -> !fileManager.exist(imageUrl))) {
            throw new ApplicationException(ErrorStatus.IMAGE_NOT_FOUND);
        }
    }
}

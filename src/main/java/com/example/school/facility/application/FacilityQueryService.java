package com.example.school.facility.application;

import com.example.school.facility.domain.Facility;
import com.example.school.facility.application.dto.FacilityResponseDTO;
import com.example.school.facility.application.dto.FacilitySaveResponseDTO;
import java.util.Optional;

public interface FacilityQueryService {

    Optional<Facility> findFacility(Long id);

    Facility createFacility(Long RegionId, FacilitySaveResponseDTO.CreateFacilityResultDTO request);

    //Page<Review> getReviewList(Long FacilityId, Integer page);
    FacilityResponseDTO.Detail getDetail(Long facilityId);

    FacilityResponseDTO.Images getImages(Long facilityId, Integer page);

    FacilityResponseDTO.ListByKeyword getListByKeyword(Long memberId, String keyword);

    FacilityResponseDTO.DetailOnMarker getDetailOnMarker(Long buildingId);

    FacilityResponseDTO.SearchResults searchFacility(Long memberId, String keyword, Integer page);

    FacilityResponseDTO.SearchLogList getSearchLog(Long memberId);

    FacilityResponseDTO.SearchRankList getSearchRank(Long memberId);

    FacilityResponseDTO.BuildingDetail getBuildingDetail(Long buildingId);

    FacilityResponseDTO.BuildingImages getBuildingImages(Long buildingId, Integer page);
}
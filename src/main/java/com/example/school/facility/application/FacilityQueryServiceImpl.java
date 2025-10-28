package com.example.school.facility.application;

import com.example.school.facility.domain.Building;
import com.example.school.facility.domain.BuildingImage;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityImage;
import com.example.school.facility.domain.FacilityKeyword;
import com.example.school.facility.domain.SearchRank;
import com.example.school.facility.application.dto.FacilityResponseDTO;
import com.example.school.facility.application.dto.FacilitySaveResponseDTO;
import com.example.school.facility.domain.BuildingImageRepository;
import com.example.school.facility.domain.BuildingRepository;
import com.example.school.facility.domain.FacilityImageRepository;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.facility.domain.SearchRankRepository;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.review.domain.Review;
import com.example.school.user.domain.Member;
import com.example.school.user.domain.ReviewRepository;
import com.example.school.user.domain.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FacilityQueryServiceImpl implements FacilityQueryService {
    private final FacilityRepository facilityRepository;
    private final BuildingImageRepository buildingImageRepository;
    private final FacilityImageRepository facilityImageRepository;
    private final UserRepository userRepository;
    private final BuildingRepository buildingRepository;
    private final SearchRankRepository searchRankRepository;
    private final ReviewRepository reviewRepository;
    private final FacilityService facilityService;
    private final RedisTemplate redisTemplate;

    @Override
    public Optional<Facility> findFacility(Long id) {
        return facilityRepository.findById(id);
    }

    @Override
    public Facility createFacility(Long RegionId, FacilitySaveResponseDTO.CreateFacilityResultDTO request) {
        return null;
    }

    /*
    @Override
    public Page<Review> getReviewList(Long FacilityId, Integer page) {

        Facility facility = facilityRepository.findById(FacilityId).get();

        Page<Review> FacilityPage = reviewRepository.findAllByFacility(facility, PageRequest.of(page, 10));
        return FacilityPage;
    }
*/
    @Override
    public FacilityResponseDTO.Detail getDetail(Long facilityId) {
        Facility facility = facilityRepository.findByIdWithDetail(facilityId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FACILITY_NOT_FOUND));

        PageRequest page = PageRequest.of(0, 5);
        Page<Review> reviews = reviewRepository.findAllByFacility(facility, page);

        return new FacilityResponseDTO.Detail(facility, reviews);
    }

    @Override
    public FacilityResponseDTO.Images getImages(Long facilityId, Integer page) {
        Pageable pageRequest = PageRequest.of(page - 1, 5);
        Page<FacilityImage> entities = facilityImageRepository.findByFacilityId(facilityId, pageRequest);

        List<String> list = entities.stream().map(entity -> entity.getImageURL()).collect(Collectors.toList());

        return new FacilityResponseDTO.Images(
                list,
                entities.getSize(),
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.isFirst(),
                entities.isLast()
        );
    }

    @Override
    public FacilityResponseDTO.ListByKeyword getListByKeyword(Long memberId, String keyword) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        FacilityKeyword keywordEnum = FacilityKeyword.valueOf(keyword.toUpperCase());

        List<Facility> entities = facilityRepository.findByKeywordAndSchool(keywordEnum, member.getSchool());

        List<FacilityResponseDTO.FacilityInKeyword> list = entities.stream().map(entity -> {
            return new FacilityResponseDTO.FacilityInKeyword(entity.getId(), entity.getName(), entity.getDescription(),
                    entity.getImageURL());
        }).collect(Collectors.toList());

        return new FacilityResponseDTO.ListByKeyword(list, list.size());
    }

    @Override
    public FacilityResponseDTO.DetailOnMarker getDetailOnMarker(Long buildingId) {
        Building entity = buildingRepository.findByIdWithBuildingHours(buildingId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BUILDING_NOT_FOUND));

        List<FacilityResponseDTO.BuildingHourDTO> hours = entity.getBuildingHours().stream().map(hour -> {
            return new FacilityResponseDTO.BuildingHourDTO(hour.getName(), hour.getOpeningTime(),
                    hour.getClosingTime());
        }).collect(Collectors.toList());

        return new FacilityResponseDTO.DetailOnMarker(entity.getName(), entity.getImageURL(), hours);
    }

    @Override
    public FacilityResponseDTO.SearchResults searchFacility(Long memberId, String keyword, Integer page) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Pageable pageRequest = PageRequest.of(page - 1, 10);
        Page<Facility> entities = facilityRepository.findByNameLikeAndSchool(keyword.trim(), member.getSchool(),
                pageRequest);
        facilityService.saveSearchLog(memberId, member.getSchool().getId(), keyword);

        return new FacilityResponseDTO.SearchResults(entities);
    }

    @Override
    public FacilityResponseDTO.SearchLogList getSearchLog(Long memberId) {
        String key = facilityService.searchLogKey(memberId);
        Set<String> set = redisTemplate.opsForZSet().reverseRange(key, 0, 9);
        List<String> list = set.stream().collect(Collectors.toList());

        return new FacilityResponseDTO.SearchLogList(list, list.size());
    }

    @Override
    public FacilityResponseDTO.SearchRankList getSearchRank(Long memberId) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<SearchRank> entities = searchRankRepository.findBySchool(member.getSchool());

        return new FacilityResponseDTO.SearchRankList(entities);
    }

    @Override
    public FacilityResponseDTO.BuildingDetail getBuildingDetail(Long buildingId) {
        Building entity = buildingRepository.findByIdWithBuildingHours(buildingId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BUILDING_NOT_FOUND));

        return new FacilityResponseDTO.BuildingDetail(entity);
    }

    @Override
    public FacilityResponseDTO.BuildingImages getBuildingImages(Long buildingId, Integer page) {
        Pageable pageRequest = PageRequest.of(page - 1, 5);
        Page<BuildingImage> entities = buildingImageRepository.findByBuildingId(buildingId, pageRequest);

        return new FacilityResponseDTO.BuildingImages(entities);
    }
}

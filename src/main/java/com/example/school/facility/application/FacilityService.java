package com.example.school.facility.application;

import com.example.school.facility.domain.Building;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityTag;
import com.example.school.facility.domain.Theme;
import com.example.school.facility.application.dto.FacilityResponseDTO;
import com.example.school.facility.domain.BuildingRepository;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.facility.domain.ThemeRepository;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.user.domain.Member;
import com.example.school.user.domain.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final ThemeRepository themeRepository;
    private final BuildingRepository buildingRepository;
    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;

    public Facility findById(Long id) {
        return facilityRepository.findById(id).get();
    }

    public FacilityResponseDTO.ListByTheme getListByTheme(Long memberId) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Theme> themeEntities = themeRepository.findBySchoolWithFacility(member.getSchool());

        List<Facility> facilityEntities = facilityRepository.findBySchoolAndIsThemeIsTrue(member.getSchool());

        List<FacilityResponseDTO.ThemeWithFacilities> themeList =
                themeEntities.stream().map(FacilityResponseDTO.ThemeWithFacilities::new).collect(Collectors.toList());

        List<FacilityResponseDTO.FacilityIdAndExtraName> facilityList =
                facilityEntities.stream().map(FacilityResponseDTO.FacilityIdAndExtraName::new)
                        .collect(Collectors.toList());

        return new FacilityResponseDTO.ListByTheme(themeList, facilityList);
    }

    public FacilityResponseDTO.ListByBuilding getListByBuilding(Long memberId) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Building> entities = buildingRepository.findBySchoolAndInCategoryWithFacility(member.getSchool());

        List<FacilityResponseDTO.BuildingWithFacilities> list =
                entities.stream().map(FacilityResponseDTO.BuildingWithFacilities::new)
                        .collect(Collectors.toList());

        return new FacilityResponseDTO.ListByBuilding(list, list.size());
    }

    public FacilityResponseDTO.Markers getMarkers(Long memberId) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Building> entities = buildingRepository.findAllBySchool(member.getSchool());

        List<FacilityResponseDTO.Marker> list = entities.stream()
                .map(entity -> new FacilityResponseDTO.Marker(entity.getId(), entity.getLabel(), entity.getLatitude(),
                        entity.getLongitude()))
                .collect(Collectors.toList());

        return new FacilityResponseDTO.Markers(list, list.size());
    }

    public FacilityResponseDTO.Tags getSuggestion(Long memberId) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Facility> entities = facilityRepository.findBySchoolAndTagIsNotNull(member.getSchool());

        Map<FacilityTag, List<Facility>> map = entities.stream().collect(Collectors.groupingBy(Facility::getTag));

        List<FacilityResponseDTO.Tag> tags = map.keySet().stream().map(key -> {
            List<FacilityResponseDTO.FacilityWithTag> list =
                    map.get(key).stream().map(value -> {
                        return new FacilityResponseDTO.FacilityWithTag(value.getId(), value.getName(),
                                value.getImageURL());
                    }).collect(Collectors.toList());
            return new FacilityResponseDTO.Tag(key.getTag(), list, list.size());
        }).collect(Collectors.toList());

        return new FacilityResponseDTO.Tags(tags);
    }

    public void saveSearchLog(Long memberId, Long schoolId, String value) {
        String key = searchLogKeyBySchool(schoolId);
        redisTemplate.opsForList().rightPush(key, value);

        key = searchLogKey(memberId);
        Long size = redisTemplate.opsForZSet().size(key);
        if (size == 10) {
            redisTemplate.opsForZSet().removeRange(key, 0, 0);
        }
        redisTemplate.opsForZSet().add(key, value, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public String searchLogKey(Long memberId) {
        return "SearchLog:" + memberId;
    }

    public String searchLogKeyBySchool(Long schoolId) {
        return "School:" + schoolId;
    }

    public FacilityResponseDTO.DeleteSearchLog deleteSearchLog(Long memberId, String value) {
        String key = searchLogKey(memberId);

        long count = redisTemplate.opsForZSet().remove(key, value);

        if (count != 1) {
            throw new GeneralException(ErrorStatus.BAD_REQUEST);
        }
        return new FacilityResponseDTO.DeleteSearchLog(value);
    }
}

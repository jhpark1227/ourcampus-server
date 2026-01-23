package com.umc.ourcampus.review.application;

import com.umc.ourcampus.facility.domain.FacilityAndHashTag;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.review.application.dto.response.HashTagResponse;
import com.umc.ourcampus.review.application.dto.response.HashTagWithFacilitiesResponse;
import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.review.domain.HashTagRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;
    private final FacilityRepository facilityRepository;

    public List<HashTagResponse> findAllHashTags() {
        return hashTagRepository.findAll()
                .stream()
                .map(HashTagResponse::from)
                .toList();
    }

    public List<HashTagWithFacilitiesResponse> getTopHashTags(int size) {
        List<HashTag> hashTags = hashTagRepository.findTopHashTags(size);
        List<FacilityAndHashTag> facilityAndHashTag = facilityRepository.findFacilityAndHashTagIdByHashTags(hashTags);
        Map<HashTag, List<Facility>> facilitiesByHashTag = facilityAndHashTag.stream()
                .collect(Collectors.groupingBy(
                        FacilityAndHashTag::hashTag,
                        Collectors.mapping(FacilityAndHashTag::facility, Collectors.toList())
                ));
        return hashTags.stream()
                .map(hashTag -> HashTagWithFacilitiesResponse.of(
                        hashTag,
                        facilitiesByHashTag.getOrDefault(hashTag, List.of())
                ))
                .toList();
    }
}

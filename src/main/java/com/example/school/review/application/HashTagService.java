package com.example.school.review.application;

import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityAndHashTag;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.review.application.dto.response.HashTagResponse;
import com.example.school.review.application.dto.response.HashTagWithFacilitiesResponse;
import com.example.school.review.domain.HashTag;
import com.example.school.review.domain.HashTagRepository;
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

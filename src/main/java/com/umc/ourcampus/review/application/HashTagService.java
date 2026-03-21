package com.umc.ourcampus.review.application;

import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.review.application.dto.response.HashTagResponse;
import com.umc.ourcampus.review.application.dto.response.HashTagWithFacilitiesResponse;
import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.review.domain.HashTagRepository;
import java.time.Instant;
import java.util.List;
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
        long seed = Instant.now().getEpochSecond() / (4 * 3600);
        List<HashTag> selected = hashTagRepository.findRandomHashTags(size, seed);
        return selected.stream()
                .map(hashTag -> HashTagWithFacilitiesResponse.of(
                        hashTag,
                        facilityRepository.findTopFacilitiesByHashTag(hashTag, 5)
                ))
                .toList();
    }
}

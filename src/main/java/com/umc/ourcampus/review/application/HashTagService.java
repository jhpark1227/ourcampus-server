package com.umc.ourcampus.review.application;

import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.review.application.dto.response.HashTagResponse;
import com.umc.ourcampus.review.application.dto.response.HashTagWithFacilitiesResponse;
import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.review.domain.HashTagRepository;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
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
    public List<HashTagWithFacilitiesResponse> getTopHashTags(long universityId, int size) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        long seed = Instant.now().getEpochSecond() / (4 * 3600);
        List<HashTag> selected = hashTagRepository.findRandomHashTags(size, seed);
        return selected.stream()
                .map(hashTag -> HashTagWithFacilitiesResponse.of(
                        hashTag,
                        facilityRepository.findTopFacilitiesByHashTag(hashTag, 5, university)
                ))
                .toList();
    }
}

package com.umc.ourcampus.review.application;

import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.review.application.dto.response.HashTagResponse;
import com.umc.ourcampus.review.domain.HashTagRepository;
import com.umc.ourcampus.university.domain.UniversityRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;
    private final UniversityRepository universityRepository;

    public List<HashTagResponse> findAllHashTags() {
        return hashTagRepository.findAll()
                .stream()
                .map(HashTagResponse::from)
                .toList();
    }

    public List<HashTagResponse> getRandomHashTags(long universityId) {
        universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        long seed = Instant.now().getEpochSecond() / (4 * 3600);
        return hashTagRepository.findRandomHashTags(5, seed)
                .stream()
                .map(HashTagResponse::from)
                .toList();
    }
}
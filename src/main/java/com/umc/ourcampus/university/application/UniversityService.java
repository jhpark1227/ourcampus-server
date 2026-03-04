package com.umc.ourcampus.university.application;

import com.umc.ourcampus.university.application.dto.response.UniversityResponse;
import com.umc.ourcampus.university.domain.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;

    public List<UniversityResponse> findAllUniversities() {
        return universityRepository.findAll().stream()
                .map(UniversityResponse::from)
                .toList();
    }
}

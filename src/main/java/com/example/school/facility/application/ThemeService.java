package com.example.school.facility.application;

import com.example.school.facility.application.dto.response.ThemeResponse;
import com.example.school.facility.domain.Theme;
import com.example.school.facility.domain.ThemeRepository;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final UniversityRepository universityRepository;

    public List<ThemeResponse> findThemesByUniversityId(Long universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        List<Theme> themes = themeRepository.findByUniversity(university);

        return themes.stream().map(ThemeResponse::from).toList();
    }
}

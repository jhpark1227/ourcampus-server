package com.umc.ourcampus.facility.application;

import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.facility.application.dto.response.ThemeResponse;
import com.umc.ourcampus.facility.domain.Theme;
import com.umc.ourcampus.facility.domain.ThemeRepository;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
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

package com.umc.ourcampus.facility.application;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.dto.request.ThemeCreateRequest;
import com.umc.ourcampus.facility.application.dto.response.ThemeResponse;
import com.umc.ourcampus.facility.domain.FacilityThemeRepository;
import com.umc.ourcampus.facility.domain.Theme;
import com.umc.ourcampus.facility.domain.ThemeRepository;
import com.umc.ourcampus.global.exception.ApplicationException;
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
    private final FacilityThemeRepository facilityThemeRepository;
    private final UniversityRepository universityRepository;

    public List<ThemeResponse> findThemesByUniversityId(Long universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        List<Theme> themes = themeRepository.findByUniversity(university);

        return themes.stream().map(ThemeResponse::from).toList();
    }

    public void createTheme(UserPrincipal principal, long universityId, ThemeCreateRequest request) {
        if (principal.universityId() != universityId) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        Theme theme = Theme.create(request.name(), university);
        themeRepository.save(theme);
    }

    public void deleteTheme(UserPrincipal principal, long themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.THEME_NOT_FOUND));
        if (!theme.getUniversity().equalId(principal.universityId())) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        facilityThemeRepository.deleteByTheme(theme);
        themeRepository.delete(theme);
    }
}

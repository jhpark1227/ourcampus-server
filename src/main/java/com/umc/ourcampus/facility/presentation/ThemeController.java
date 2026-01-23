package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.facility.application.ThemeService;
import com.umc.ourcampus.facility.application.dto.response.ThemeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping("/universities/{universityId}/themes")
    public List<ThemeResponse> getThemes(@PathVariable("universityId") long universityId) {
        return themeService.findThemesByUniversityId(universityId);
    }
}

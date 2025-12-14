package com.example.school.facility.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.facility.application.ThemeService;
import com.example.school.facility.application.dto.response.ThemeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping("/me/themes")
    public List<ThemeResponse> getThemes(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return themeService.findThemesByUniversityId(memberPrincipal.universityId());
    }
}

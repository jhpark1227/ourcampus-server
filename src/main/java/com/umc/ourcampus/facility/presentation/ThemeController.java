package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.ThemeService;
import com.umc.ourcampus.facility.application.dto.request.ThemeCreateRequest;
import com.umc.ourcampus.facility.application.dto.request.UpdateThemeNameRequest;
import com.umc.ourcampus.facility.application.dto.response.ThemeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @PostMapping("/admin/universities/{universityId}/themes")
    public void createTheme(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("universityId") long universityId,
            @RequestBody ThemeCreateRequest request
    ) {
        themeService.createTheme(principal, universityId, request);
    }

    @GetMapping("/universities/{universityId}/themes")
    public List<ThemeResponse> getThemes(@PathVariable("universityId") long universityId) {
        return themeService.findThemesByUniversityId(universityId);
    }

    @PatchMapping("/admin/themes/{themeId}")
    public ResponseEntity<Void> updateThemeName(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("themeId") long themeId,
            @RequestBody UpdateThemeNameRequest request
    ) {
        themeService.updateThemeName(principal, themeId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/themes/{themeId}")
    public ResponseEntity<Void> deleteTheme(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("themeId") long themeId
    ) {
        themeService.deleteTheme(principal, themeId);
        return ResponseEntity.noContent().build();
    }
}

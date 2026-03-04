package com.umc.ourcampus.university.presentation;

import com.umc.ourcampus.university.application.UniversityService;
import com.umc.ourcampus.university.application.dto.response.UniversityResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping("/universities")
    public List<UniversityResponse> getUniversities() {
        return universityService.findAllUniversities();
    }
}

package com.example.school.university.presentation;

import com.example.school.university.application.UniversityService;
import com.example.school.university.application.dto.response.DepartmentResponse;
import com.example.school.university.application.dto.response.UniversityResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping
    public List<UniversityResponse> getUniversities() {
        return universityService.findAllUniversities();
    }

    @GetMapping("/{universityId}/departments")
    public List<DepartmentResponse> getDepartments(@PathVariable(name = "universityId") long universityId) {
        return universityService.findDepartmentsByUniversityId(universityId);
    }
}

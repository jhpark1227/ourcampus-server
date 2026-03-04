package com.umc.ourcampus.university.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.university.application.DepartmentService;
import com.umc.ourcampus.university.application.dto.request.CreateDepartmentRequest;
import com.umc.ourcampus.university.application.dto.request.UpdateDepartmentNameRequest;
import com.umc.ourcampus.university.application.dto.response.DepartmentResponse;
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
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/admin/university/{universityId}/departments")
    public void createDepartment(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable long universityId,
            @RequestBody CreateDepartmentRequest request
    ) {
        departmentService.createDepartment(principal, universityId, request);
    }

    @GetMapping("/universities/{universityId}/departments")
    public List<DepartmentResponse> getUniversityDepartments(@PathVariable(name = "universityId") long universityId) {
        return departmentService.getUniversityDepartments(universityId);
    }

    @PatchMapping("/admin/departments/{departmentId}")
    public ResponseEntity<Void> updateDepartmentName(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable long departmentId,
            @RequestBody UpdateDepartmentNameRequest request
    ) {
        departmentService.updateDepartmentName(principal, departmentId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/departments/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable long departmentId
    ) {
        departmentService.deleteDepartment(principal, departmentId);
        return ResponseEntity.noContent().build();
    }
}

package com.umc.ourcampus.university.application;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.member.domain.MemberRepository;
import com.umc.ourcampus.university.application.dto.request.CreateDepartmentRequest;
import com.umc.ourcampus.university.application.dto.request.UpdateDepartmentNameRequest;
import com.umc.ourcampus.university.application.dto.response.DepartmentResponse;
import com.umc.ourcampus.university.domain.Department;
import com.umc.ourcampus.university.domain.DepartmentRepository;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UniversityRepository universityRepository;
    private final MemberRepository memberRepository;

    public void createDepartment(UserPrincipal principal, long universityId, CreateDepartmentRequest request) {
        if (principal.universityId() != universityId) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        if (!departmentRepository.existsByUniversityAndName(university, request.name())) {
            throw new ApplicationException(ErrorStatus.DUPLICATED_DEPARTMENT);
        }
        Department department = new Department(request.name(), university);
        departmentRepository.save(department);
    }

    public List<DepartmentResponse> getUniversityDepartments(long universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        return departmentRepository.findByUniversity(university)
                .stream()
                .map(DepartmentResponse::from)
                .toList();
    }

    public void deleteDepartment(UserPrincipal principal, long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.DEPARTMENT_NOT_FOUND));
        if (!department.getUniversity().equalId(principal.universityId())) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        if (!memberRepository.findByDepartment(department).isEmpty()) {
            throw new ApplicationException(ErrorStatus.MEMBER_EXIST);
        }
        departmentRepository.delete(department);
    }

    public void updateDepartmentName(UserPrincipal principal, long departmentId, UpdateDepartmentNameRequest request) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.DEPARTMENT_NOT_FOUND));
        if (!department.getUniversity().equalId(principal.universityId())) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        if (!departmentRepository.existsByUniversityAndName(department.getUniversity(), request.name())) {
            throw new ApplicationException(ErrorStatus.DUPLICATED_DEPARTMENT);
        }
        department.changeName(request.name());
    }
}

package com.example.school.university.application;

import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.university.application.dto.response.DepartmentResponse;
import com.example.school.university.application.dto.response.UniversityResponse;
import com.example.school.university.domain.DepartmentRepository;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final DepartmentRepository departmentRepository;

    public List<UniversityResponse> findAllUniversities() {
        return universityRepository.findAll().stream()
                .map(UniversityResponse::from)
                .toList();
    }

    public List<DepartmentResponse> findDepartmentsByUniversityId(long universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        return departmentRepository.findByUniversity(university)
                .stream()
                .map(DepartmentResponse::from)
                .toList();
    }
}

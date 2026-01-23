package com.umc.ourcampus.university.application;

import com.umc.ourcampus.university.application.dto.response.DepartmentResponse;
import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.university.application.dto.response.UniversityResponse;
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
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final DepartmentRepository departmentRepository;

    public List<UniversityResponse> findAllUniversities() {
        return universityRepository.findAll().stream()
                .map(UniversityResponse::from)
                .toList();
    }

    public List<DepartmentResponse> findDepartmentsByUniversity(long universityId) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        return departmentRepository.findByUniversity(university)
                .stream()
                .map(DepartmentResponse::from)
                .toList();
    }
}

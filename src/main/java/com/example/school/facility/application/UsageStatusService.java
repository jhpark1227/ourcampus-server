package com.example.school.facility.application;

import com.example.school.facility.application.dto.response.UsageStatusResponse;
import com.example.school.facility.domain.UsageStatus;
import com.example.school.facility.domain.UsageStatusRepository;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsageStatusService {

    private final UsageStatusRepository usageStatusRepository;
    private final UniversityRepository universityRepository;
    private final List<UsageStatusCollector> collectors;

    @Transactional(readOnly = true)
    public Page<UsageStatusResponse> getUsageStatus(long universityId, Pageable pageable) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        return usageStatusRepository.findLastestByUniversity(university, pageable)
                .map(UsageStatusResponse::from);
    }

    public void updateAllUniversities() {
        universityRepository.findAll().forEach(university -> {
            try {
                updateUniversityData(university);
            } catch (Exception e) {
                log.error("Failed to update usage status for university: {} ({})", university.getId(), university.getName(), e);
            }
        });
    }

    private void updateUniversityData(University university) {
        List<UsageStatus> collectedData = collectors.stream()
                .filter(collector -> collector.supports(university))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorStatus.LIVE_SEAT_NOT_SUPPORTED))
                .collect();
        usageStatusRepository.saveAll(collectedData);
    }
}

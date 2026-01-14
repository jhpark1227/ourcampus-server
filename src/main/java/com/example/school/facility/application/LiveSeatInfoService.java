package com.example.school.facility.application;

import com.example.school.facility.application.dto.response.LiveSeatInfoResponse;
import com.example.school.facility.domain.LiveSeatInfoRepository;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LiveSeatInfoService {

    private final LiveSeatInfoRepository liveSeatInfoRepository;
    private final UniversityRepository universityRepository;

    public Page<LiveSeatInfoResponse> getLiveSeatInfo(long universityId, Pageable pageable) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        return liveSeatInfoRepository.findByFacility_University(university, pageable)
                .map(LiveSeatInfoResponse::from);
    }
}

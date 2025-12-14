package com.example.school.facility.application;

import com.example.school.facility.application.dto.ScoreDTO;
import com.example.school.facility.domain.FacilityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewScoreService {
    private final FacilityRepository facilityRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void updateScore() {
        List<ScoreDTO> list = facilityRepository.findAllWithReview();
        list.forEach(dto -> {
        });
    }
}

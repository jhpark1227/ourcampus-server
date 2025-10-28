package com.example.school.facility.application;

import com.example.school.facility.domain.School;
import com.example.school.facility.domain.SearchLog;
import com.example.school.facility.domain.SearchRank;
import com.example.school.facility.domain.SchoolRepository;
import com.example.school.facility.domain.SearchLogRepository;
import com.example.school.facility.domain.SearchRankRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SearchRankService {
    private final RedisTemplate redisTemplate;
    private final SearchLogRepository searchLogRepository;
    private final SchoolRepository schoolRepository;
    private final SearchRankRepository searchRankRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void storeRank() {
        storeCount();
        List<School> schoolList = schoolRepository.findAll();
        for (School school : schoolList) {
            List<SearchLog> list = searchLogRepository.findTop5BySchoolOrderByCountDesc(school);
            searchRankRepository.deleteBySchool(school);
            for (int i = 0; i < list.size(); i++) {
                searchRankRepository.save(
                        SearchRank.builder()
                                .ranking(i + 1)
                                .value(list.get(i).getValue())
                                .school(school)
                                .build()
                );
            }
        }
        log.info("인기 검색어 갱신 완료");
    }

    public void storeCount() {
        Set<String> schoolList = redisTemplate.keys("School:*");
        for (String key : schoolList) {
            School school = schoolRepository.findById(getSchoolId(key))
                    .orElseThrow();

            Long size = redisTemplate.opsForList().size(key);
            List<String> list = redisTemplate.opsForList().range(key, 0, size - 1);

            for (String value : list) {
                Optional<SearchLog> log = searchLogRepository.findByValueAndSchool(value, school);
                if (log.isPresent()) {
                    log.get().plusCount();
                } else {
                    searchLogRepository.save(
                            SearchLog.builder()
                                    .value(value)
                                    .count(1L)
                                    .school(school)
                                    .build()
                    );
                }
            }
            redisTemplate.delete(key);
        }
    }

    public Long getSchoolId(String key) {
        System.out.println(Long.parseLong(key.substring(7)));
        return Long.parseLong(key.substring(7));
    }
}

package com.example.school.facility.application;

import com.example.school.facility.application.dto.response.HashTagResponse;
import com.example.school.facility.domain.HashTagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;

    public List<HashTagResponse> getTopTags(int size) {
        return null;
    }
}

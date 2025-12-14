package com.example.school.facility.presentation;

import com.example.school.facility.application.HashTagService;
import com.example.school.facility.application.dto.response.HashTagResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HashTagController {

    private final HashTagService hashTagService;

    @GetMapping("/hashtags")
    public List<HashTagResponse> getTopTags(@RequestParam(name = "size") int size) {
        return hashTagService.getTopTags(size);
    }
}

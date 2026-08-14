package com.umc.ourcampus.review.presentation;

import com.umc.ourcampus.review.application.HashTagService;
import com.umc.ourcampus.review.application.dto.response.HashTagResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HashTagController {

    private final HashTagService hashTagService;

    @GetMapping("/hashtags")
    public List<HashTagResponse> getHashTags() {
        return hashTagService.findAllHashTags();
    }

    @GetMapping("/universities/{universityId}/hashtags/random")
    public List<HashTagResponse> getRandomHashTags(
            @PathVariable(name = "universityId") long universityId) {
        return hashTagService.getRandomHashTags(universityId);
    }
}
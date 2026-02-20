package com.umc.ourcampus.review.presentation;

import com.umc.ourcampus.review.application.HashTagService;
import com.umc.ourcampus.review.application.dto.response.HashTagResponse;
import com.umc.ourcampus.review.application.dto.response.HashTagWithFacilitiesResponse;
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
    public List<HashTagResponse> getHashTags() {
        return hashTagService.findAllHashTags();
    }

    @GetMapping("/hashtags/popular")
    public List<HashTagWithFacilitiesResponse> getTopTags(@RequestParam(name = "size") int size) {
        return hashTagService.getTopHashTags(size);
    }
}

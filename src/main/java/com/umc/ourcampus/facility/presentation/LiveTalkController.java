package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.LiveTalkService;
import com.umc.ourcampus.facility.application.dto.request.LiveTalkRequest;
import com.umc.ourcampus.facility.application.dto.response.LiveTalkResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LiveTalkController {

    private final LiveTalkService liveTalkService;

    @PostMapping("/facilities/{facilityId}/live-talk")
    public ResponseEntity<Void> createLiveTalk(
            @PathVariable("facilityId") long facilityId,
            @RequestBody @Valid LiveTalkRequest liveTalkRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        long liveTalkId = liveTalkService.createLiveTalk(userPrincipal.memberId(), facilityId, liveTalkRequest);
        return ResponseEntity.created(URI.create("/live-talk/" + liveTalkId)).build();
    }

    @GetMapping("/facilities/{facilityId}/live-talk")
    public Page<LiveTalkResponse> getFacilityLiveTalk(
            @PathVariable("facilityId") long facilityId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam("size") int size
    ) {
        return liveTalkService.getFacilityLiveTalk(facilityId, PageRequest.of(page, size));
    }
}

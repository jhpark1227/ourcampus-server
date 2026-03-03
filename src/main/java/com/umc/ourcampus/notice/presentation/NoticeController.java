package com.umc.ourcampus.notice.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.notice.application.NoticeService;
import com.umc.ourcampus.notice.application.dto.request.CreateNoticeRequest;
import com.umc.ourcampus.notice.application.dto.request.UpdateNoticeRequest;
import com.umc.ourcampus.notice.application.dto.response.NoticeResponse;
import com.umc.ourcampus.notice.domain.NoticeType;
import com.umc.ourcampus.notice.presentation.dto.response.NoticeTypeResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/admin/universities/{universityId}/notices")
    public void createNotice(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("universityId") long universityId,
            @RequestBody CreateNoticeRequest request
    ) {
        noticeService.createNotice(principal, universityId, request);
    }

    @GetMapping("/universities/{universityId}/notices")
    public Page<NoticeResponse> getNotices(
            @PathVariable("universityId") Long universityId,
            @RequestParam(name = "type", required = false) NoticeType type,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return noticeService.findNotices(universityId, type, pageable);
    }

    @GetMapping("/notices/types")
    public List<NoticeTypeResponse> getNoticeTypes() {
        return Arrays.stream(NoticeType.values())
                .map(NoticeTypeResponse::from)
                .toList();
    }

    @GetMapping("/notices/{id}")
    public ResponseEntity<NoticeResponse> getNotice(@PathVariable("id") Long id) {
        NoticeResponse response = noticeService.findNoticeById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/notices/{noticeId}")
    public ResponseEntity<Void> updateNotice(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("noticeId") long noticeId,
            @RequestBody UpdateNoticeRequest request
    ) {
        noticeService.updateNotice(principal, noticeId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/notices/{noticeId}")
    public ResponseEntity<Void> deleteNotice(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("noticeId") long noticeId
    ) {
        noticeService.deleteNotice(principal, noticeId);
        return ResponseEntity.noContent().build();
    }
}

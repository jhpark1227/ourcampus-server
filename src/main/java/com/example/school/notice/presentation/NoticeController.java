package com.example.school.notice.presentation;

import com.example.school.notice.application.NoticeService;
import com.example.school.notice.application.dto.response.NoticeDetailResponse;
import com.example.school.notice.application.dto.response.NoticeResponse;
import com.example.school.notice.domain.NoticeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/universities/{universityId}/notices")
    public Page<NoticeResponse> getNotices(
            @PathVariable("universityId") Long universityId,
            @RequestParam(name = "type", required = false) NoticeType type,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return noticeService.findNotices(universityId, type, pageable);
    }

    @GetMapping("/notices/{id}")
    public ResponseEntity<NoticeDetailResponse> getNotice(@PathVariable("id") Long id) {
        NoticeDetailResponse response = noticeService.findNoticeById(id);

        return ResponseEntity.ok(response);
    }
}

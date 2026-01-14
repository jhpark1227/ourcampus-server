package com.example.school.facility.application.dto.response;

import com.example.school.facility.domain.LiveTalk;
import com.example.school.member.domain.Member;
import java.time.LocalDateTime;

public record LiveTalkResponse(
        long id,
        String message,
        LocalDateTime createdAt,
        MemberResponse member
) {
    public static LiveTalkResponse from(LiveTalk liveTalk) {
        return new LiveTalkResponse(
                liveTalk.getId(),
                liveTalk.getMessage(),
                liveTalk.getCreatedAt(),
                MemberResponse.from(liveTalk.getMember())
        );
    }

    private record MemberResponse(
            String name,
            String profileImage
    ) {
        private static MemberResponse from(Member member) {
            return new MemberResponse(
                    member.getName(),
                    member.getProfileImage()
            );
        }
    }
}

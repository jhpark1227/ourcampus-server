package com.example.school.member.application.dto.response;

import com.example.school.member.domain.Member;

public record MemberInfoResponse(
        String name,
        String profileImage,
        Long universityId
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(member.getName(), member.getProfileImage(), member.getUniversity().getId());
    }
}

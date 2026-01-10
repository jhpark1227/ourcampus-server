package com.example.school.member.application.dto.response;

import com.example.school.member.domain.Member;
import com.example.school.university.domain.University;

public record MemberInfoResponse(
        String name,
        String studentId,
        String profileImage,
        String department,
        UniversityResponse university
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getName(),
                member.getStudentId(),
                member.getProfileImage(),
                member.getDepartment().getName(),
                UniversityResponse.from(member.getUniversity())
        );
    }

    private record UniversityResponse(
            long id,
            String name
    ) {
        private static UniversityResponse from(University university) {
            return new UniversityResponse(university.getId(), university.getName());
        }
    }
}

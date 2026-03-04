package com.umc.ourcampus.member.application.dto.response;

import com.umc.ourcampus.member.domain.Admin;

public record AdminInfoResponse(
        String name,
        UniversityResponse university
) {
    public static AdminInfoResponse from(Admin admin) {
        return new AdminInfoResponse(
                admin.getName(),
                new UniversityResponse(
                        admin.getUniversity().getId(),
                        admin.getUniversity().getName()
                )
        );
    }

    private record UniversityResponse(
            long id,
            String name
    ) {
    }
}

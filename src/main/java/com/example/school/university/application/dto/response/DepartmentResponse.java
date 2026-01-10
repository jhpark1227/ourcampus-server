package com.example.school.university.application.dto.response;

import com.example.school.university.domain.Department;

public record DepartmentResponse(
        long id,
        String name
) {
    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(department.getId(), department.getName());
    }
}

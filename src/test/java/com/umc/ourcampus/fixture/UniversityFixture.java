package com.umc.ourcampus.fixture;

import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.Department;

public class UniversityFixture {

    public static University createUniversity() {
        return createUniversity("테스트대학교");
    }

    public static University createUniversity(String name) {
        return new University(name);
    }

    public static Department createDepartment(University university) {
        return new Department("전공1", university);
    }
}

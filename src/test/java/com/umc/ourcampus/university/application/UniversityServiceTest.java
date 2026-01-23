package com.umc.ourcampus.university.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.umc.ourcampus.university.domain.DepartmentRepository;
import com.umc.ourcampus.fixture.UniversityFixture;
import com.umc.ourcampus.university.application.dto.response.DepartmentResponse;
import com.umc.ourcampus.university.application.dto.response.UniversityResponse;
import com.umc.ourcampus.university.domain.Department;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UniversityServiceTest {

    @Autowired
    UniversityService sut;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    void 모든_대학교를_조회한다() {
        universityRepository.save(UniversityFixture.createUniversity("테스트대학교1"));
        universityRepository.save(UniversityFixture.createUniversity("테스트대학교2"));
        universityRepository.save(UniversityFixture.createUniversity("테스트대학교3"));

        List<UniversityResponse> universities = sut.findAllUniversities();

        assertThat(universities).hasSize(3);
    }

    @Test
    void 대학교의_모든_전공을_조회한다() {
        University university = universityRepository.save(UniversityFixture.createUniversity("테스트대학교1"));
        departmentRepository.save(new Department("전공1", university));
        departmentRepository.save(new Department("전공2", university));
        departmentRepository.save(new Department("전공3", university));

        List<DepartmentResponse> departments = sut.findDepartmentsByUniversity(university.getId());

        assertThat(departments).hasSize(3);
    }
}
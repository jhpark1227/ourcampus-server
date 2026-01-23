package com.umc.ourcampus.reservation.application;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.umc.ourcampus.fixture.FacilityFixture;
import com.umc.ourcampus.university.domain.Department;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.fixture.MemberFixture;
import com.umc.ourcampus.fixture.UniversityFixture;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.member.domain.MemberRepository;
import com.umc.ourcampus.reservation.application.dto.request.ReservationRequest;
import com.umc.ourcampus.reservation.domain.ReservationPolicy;
import com.umc.ourcampus.university.domain.DepartmentRepository;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
import java.time.LocalDateTime;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    ReservationService sut;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FacilityRepository facilityRepository;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    void 예약을_생성한다() {
        University university = universityRepository.save(UniversityFixture.createUniversity());
        Department department = departmentRepository.save(UniversityFixture.createDepartment(university));
        Member member = memberRepository.save(MemberFixture.create(department));
        ReservationPolicy policy = FacilityFixture.createReservablePolicy(9, 18);
        Facility facility = facilityRepository.save(FacilityFixture.create(university, policy));
        ReservationRequest request = new ReservationRequest(
                facility.getId(),
                1,
                LocalDateTime.of(2025, 12, 2, 12, 0),
                60,
                Set.of()
        );

        assertThatCode(() -> sut.createReservation(request, member.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    void 최대_예약시간보다_긴_시간_예약하면_예외가_발생한다() {
        University university = universityRepository.save(UniversityFixture.createUniversity());
        Department department = departmentRepository.save(UniversityFixture.createDepartment(university));
        Member member = memberRepository.save(MemberFixture.create(department));
        Facility facility = facilityRepository.save(FacilityFixture.create(university));
        ReservationRequest request = new ReservationRequest(
                facility.getId(),
                1,
                LocalDateTime.of(2025, 12, 2, 12, 0),
                181,
                Set.of()
        );

        Assertions.assertThatThrownBy(() -> sut.createReservation(request, member.getId()))
                .isInstanceOf(ApplicationException.class);
    }
}
package com.example.school.reservation.application;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.facility.domain.OperationTime;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.reservation.application.dto.request.ReservationRequest;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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

    @Test
    void 예약을_생성한다() {
        University university = universityRepository.save(new University("대학교이름"));
        Member member = Member.builder()
                .name("회원1")
                .email("email@email.com")
                .university(university)
                .build();
        memberRepository.save(member);
        Facility facility = Facility.builder()
                .name("시설1")
                .university(university)
                .operationTimes(List.of(new OperationTime("시간1", LocalTime.of(9, 0), LocalTime.of(20, 0))))
                .build();
        facilityRepository.save(facility);
        ReservationRequest request = new ReservationRequest(
                facility.getId(), 1, LocalDateTime.of(2025, 12, 2, 12, 0), 60, Set.of());
        MemberPrincipal memberPrincipal = new MemberPrincipal(member.getId(), university.getId());

        assertThatCode(() -> sut.createReservation(request, memberPrincipal))
                .doesNotThrowAnyException();
    }

    @Test
    void 최대_예약시간보다_긴_시간_예약하면_예외가_발생한다() {
        University university = universityRepository.save(new University("대학교이름"));
        Member member = Member.builder()
                .name("회원1")
                .email("email@email.com")
                .university(university)
                .build();
        memberRepository.save(member);
        Facility facility = Facility.builder()
                .name("시설1")
                .university(university)
                .operationTimes(List.of(new OperationTime("시간1", LocalTime.of(9, 0), LocalTime.of(20, 0))))
                .build();
        facilityRepository.save(facility);
        ReservationRequest request = new ReservationRequest(
                facility.getId(), 1, LocalDateTime.of(2025, 12, 2, 12, 0), 181, Set.of());
        MemberPrincipal memberPrincipal = new MemberPrincipal(member.getId(), university.getId());

        Assertions.assertThatThrownBy(() -> sut.createReservation(request, memberPrincipal))
                .isInstanceOf(ApplicationException.class);
    }
}
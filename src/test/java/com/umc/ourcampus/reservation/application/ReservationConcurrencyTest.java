package com.umc.ourcampus.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.fixture.FacilityFixture;
import com.umc.ourcampus.fixture.MemberFixture;
import com.umc.ourcampus.fixture.UniversityFixture;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.member.domain.MemberRepository;
import com.umc.ourcampus.reservation.application.dto.request.ReservationRequest;
import com.umc.ourcampus.reservation.domain.ReservationRepository;
import com.umc.ourcampus.support.DatabaseCleaner;
import com.umc.ourcampus.university.domain.Department;
import com.umc.ourcampus.university.domain.DepartmentRepository;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationConcurrencyTest {

    @Autowired
    ReservationFacadeService sut;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FacilityRepository facilityRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() {
        databaseCleaner.clean();
    }

    @Test
    void 동시에_같은_시간대를_예약하면_1건만_성공해야_한다() throws InterruptedException {
        int threadCount = 10;

        University university = universityRepository.save(UniversityFixture.createUniversity());
        Department department = departmentRepository.save(UniversityFixture.createDepartment(university));
        Facility facility = facilityRepository.save(
                FacilityFixture.create(university, FacilityFixture.createReservablePolicy(9, 18))
        );

        List<Member> members = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            members.add(memberRepository.save(MemberFixture.create(department, i)));
        }

        ReservationRequest request = new ReservationRequest(
                facility.getId(),
                1,
                LocalDateTime.of(2025, 12, 2, 10, 0),
                60,
                Set.of()
        );

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            long memberId = members.get(i).getId();
            executor.submit(() -> {
                try {
                    startLatch.await();
                    sut.createReservation(request, memberId);
                    successCount.incrementAndGet();
                } catch (Exception ignored) {
                    failCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        long savedCount = reservationRepository.count();

        assertThat(savedCount).isEqualTo(1);
    }
}
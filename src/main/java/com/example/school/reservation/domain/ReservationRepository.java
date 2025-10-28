package com.example.school.reservation.domain;

import com.example.school.user.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findAllByMemberId(Long memberId, PageRequest pageRequest);

    Page<Reservation> findAllByFacilityId(Long facilityId, PageRequest pageRequest);

    List<Reservation> findAllByMemberId(Long memberId);

    List<Reservation> findAllByFacilityIdAndYearAndMonthAndDay(Long facilityId, String year, String month, String day);

    @Query("select r " +
            "from Reservation r " +
            "where r.member=:member " +
            "and r.year=:year and r.month=:month and r.day=:day " +
            "and r.start_time<=:hour and r.end_time>:hour")
    Optional<Reservation> findInUse(Member member, String year, String month, String day, int hour);
}

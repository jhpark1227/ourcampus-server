package com.example.school.reservation.domain;

import com.example.school.facility.domain.Facility;
import com.example.school.member.domain.Member;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
                SELECT r
                FROM Reservation r
                WHERE r.facility = :facility
                AND DATE_FORMAT(r.timeSlot.startTime, '%Y-%m-%d') = DATE_FORMAT(str_to_date(:date, '%Y-%m-%d'), '%Y-%m-%d')
            """)
    List<Reservation> findByFacilityAndDate(Facility facility, LocalDate date);

    @Query("""
                    SELECT reservation
                    FROM Reservation reservation
                    LEFT JOIN Review review ON review.reservation.id = reservation.id
                    WHERE reservation.member = :member
                    AND review IS NULL
            """)
    List<Reservation> findReservationWithoutReviewByMember(Member member);

    List<Reservation> findByMember(Member member);
}

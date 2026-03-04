package com.umc.ourcampus.reservation.domain;

import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.member.domain.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
                SELECT r
                FROM Reservation r
                WHERE r.facility = :facility
                AND CAST(r.timeSlot.startTime AS DATE) = CAST(:date AS DATE)
            """)
    List<Reservation> findByFacilityAndDate(Facility facility, LocalDate date);

    @Query("""
                    SELECT reservation
                    FROM Reservation reservation
                    LEFT JOIN Review review ON review.reservation.id = reservation.id
                    WHERE reservation.member = :member
                    AND reservation.status = 'RETURNED'
                    AND review IS NULL
            """)
    List<Reservation> findReturnedReservationsWithoutReviewByMember(Member member);

    List<Reservation> findByMember(Member member);

    @Query("""
            SELECT reservation
            FROM Reservation reservation
            WHERE reservation.timeSlot.startTime <= NOW()
            AND NOW() <= reservation.timeSlot.endTime
            AND reservation.member = :member
            AND reservation.status = 'RESERVED'
            """)
    Optional<Reservation> findInUseReservationByMember(Member member);

    void deleteByMember(Member member);

    void deleteByFacility(Facility facility);
}

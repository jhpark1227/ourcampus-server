package com.example.school.reservation.domain;

import com.example.school.alarm.domain.Alarm;
import com.example.school.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("""
                SELECT alarm
                FROM Alarm alarm
                WHERE alarm.member = :member
                AND alarm.scheduledTime <= NOW()
            """)
    List<Alarm> findSendAlarmByMember(Member member);

    @Query("""
                SELECT COUNT(*)
                FROM Alarm alarm
                WHERE alarm.member = :member
                AND alarm.checked = false
                AND alarm.scheduledTime <= NOW()
            """)
    long findUnreadCount(Member member);
}

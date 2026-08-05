package com.umc.ourcampus.alarm.domain;

import com.umc.ourcampus.member.domain.Member;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {

    @Query("""
                SELECT COUNT(*)
                FROM Alarm alarm
                WHERE alarm.member = :member
                AND alarm.checked = false
                AND alarm.scheduledTime <= NOW()
            """)
    long findUnreadCount(Member member);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                UPDATE Alarm alarm
                SET alarm.checked = true
                WHERE alarm.member = :member
                AND alarm.checked = false
                AND alarm.scheduledTime <= NOW()
            """)
    int markAllAsRead(Member member);

    @Modifying
    @Query(value = """
                DELETE FROM alarm
                WHERE scheduled_time < :threshold
                LIMIT :batchSize
            """, nativeQuery = true)
    int deleteAlarmsScheduledBefore(LocalDateTime threshold, int batchSize);
}
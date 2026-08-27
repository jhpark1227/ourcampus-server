package com.umc.ourcampus.reservation.domain;

import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.reservation.application.ReservationLock;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Repository
@RequiredArgsConstructor
public class MysqlReservationNamedLock implements ReservationLock {

    private final JdbcTemplate jdbcTemplate;

    public void getLock(long facilityId, LocalDate date) {
        String key = "reservation:" + facilityId + ":" + date;
        Integer result = jdbcTemplate.queryForObject("SELECT GET_LOCK(?, 3)", Integer.class, key);
        if (result == null || result != 1) {
            throw new ApplicationException(ErrorStatus.LOCK_ACQUISITION_FAILED);
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                releaseLock(facilityId, date);
            }
        });
    }

    private void releaseLock(long facilityId, LocalDate date) {
        String key = "reservation:" + facilityId + ":" + date;
        jdbcTemplate.queryForObject("SELECT RELEASE_LOCK(?)", Integer.class, key);
    }
}

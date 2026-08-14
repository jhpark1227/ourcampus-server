package com.umc.ourcampus.reservation.domain;

import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NamedLockRepository {

    private final JdbcTemplate jdbcTemplate;

    public void getLock(String key) {
        Integer result = jdbcTemplate.queryForObject("SELECT GET_LOCK(?, 3)", Integer.class, key);
        if (result == null || result != 1) {
            throw new ApplicationException(ErrorStatus.LOCK_ACQUISITION_FAILED);
        }
    }

    public void releaseLock(String key) {
        jdbcTemplate.queryForObject("SELECT RELEASE_LOCK(?)", Integer.class, key);
    }
}

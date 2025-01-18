package com.marketplace.auth.utils;

import com.marketplace.auth.repository.InvalidRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class CronJobs {
    private final InvalidRefreshTokenRepository invalidRefreshTokenRepository;

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000, initialDelay = 0) // Running the job once per day
    public void cleanupExpiredTokens() {
        log.debug("Enter cleanupExpiredTokens()");
        invalidRefreshTokenRepository.deleteAllExpiredBefore(Date.from(Instant.now()));
        log.debug("Leave cleanupExpiredTokens()");
    }
}

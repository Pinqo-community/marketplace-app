package com.marketplace.auth.utils;

import com.marketplace.auth.repository.InvalidRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

/**
 * Component handling scheduled maintenance tasks.
 * Manages periodic cleanup operations for the application.
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class CronJobs {
    private final InvalidRefreshTokenRepository invalidRefreshTokenRepository;

    /**
     * Cleanup task that removes expired refresh tokens from the database.
     * Runs automatically once every 24 hours.
     */
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000, initialDelay = 0)
    public void cleanupExpiredTokens() {
        log.atDebug().log("Starting expired tokens cleanup");
        invalidRefreshTokenRepository.deleteAllExpiredBefore(Date.from(Instant.now()));
        log.atDebug().log("Completed expired tokens cleanup");
    }
}

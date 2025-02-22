package com.marketplace.auth.utils;

import com.marketplace.auth.repository.InvalidRefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CronJobsTest {

    @Mock
    private InvalidRefreshTokenRepository invalidRefreshTokenRepository;

    @InjectMocks
    private CronJobs cronJobs;

    @Test
    void cleanupExpiredTokens_ShouldCallRepository() {
        // When
        cronJobs.cleanupExpiredTokens();

        // Then
        verify(invalidRefreshTokenRepository, times(1)).deleteAllExpiredBefore(any(Date.class));
    }
}
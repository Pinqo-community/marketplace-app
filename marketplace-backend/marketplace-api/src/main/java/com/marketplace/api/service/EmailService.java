package com.marketplace.api.service;

import com.marketplace.api.dto.email.EmailRequest;

/**
 * Service for handling email operations.
 * Provides both synchronous and asynchronous email sending capabilities.
 */
public interface EmailService {
    /**
     * Sends an email asynchronously without waiting for the result.
     *
     * @param request the email request to send
     */
    void sendEmailAsync(EmailRequest request);

    /**
     * Sends an email synchronously and waits for the result.
     *
     * @param request the email request to send
     */
    void sendEmailSync(EmailRequest request);
}

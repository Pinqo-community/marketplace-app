package com.marketplace.api.service;

import com.marketplace.api.dto.email.EmailRequest;

/**
 * Service for queuing emails in RabbitMQ for later processing.
 */
public interface RabbitQueueService {
    /**
     * Adds an email to the processing queue.
     *
     * @param emailRequest the email to queue
     */
    void queueEmail(EmailRequest emailRequest);
}

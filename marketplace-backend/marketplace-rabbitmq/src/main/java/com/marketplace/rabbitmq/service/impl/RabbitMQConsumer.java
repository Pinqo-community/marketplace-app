package com.marketplace.rabbitmq.service.impl;

import com.marketplace.api.dto.email.EmailRequest;
import com.marketplace.api.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.marketplace.rabbitmq.config.RabbitMQConfig.EMAIL_QUEUE;

/**
 * Consumer service that processes emails from RabbitMQ queues.
 * Handles both initial email processing and retry attempts from the Dead Letter Queue.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {
    private final EmailService emailService;

    /**
     * Processes emails from the main queue asynchronously.
     * Failed attempts are automatically sent to the DLQ.
     */
    @RabbitListener(queues = EMAIL_QUEUE)
    public void processEmail(EmailRequest emailRequest) {
        try {
            emailService.sendEmailAsync(emailRequest);
            log.atInfo().log("Successfully processed email for: {}", emailRequest.to());
        } catch (Exception e) {
            log.atError().log("Failed to process email for: {}", emailRequest.to(), e);
            // Send email to DLQ automatically
            throw e;
        }
    }

    /**
     * Processes failed emails from the Dead Letter Queue synchronously.
     * This is the final attempt to send the email.
     */
    @RabbitListener(queues = "email.dlq")
    public void processDLQ(EmailRequest emailRequest) {
        log.atWarn().log("Processing failed email from DLQ for: {}", emailRequest.to());
        try {
            emailService.sendEmailSync(emailRequest);
        } catch (Exception e) {
            log.atError().log("Final failure processing email for: {}", emailRequest.to(), e);
        }
    }
}

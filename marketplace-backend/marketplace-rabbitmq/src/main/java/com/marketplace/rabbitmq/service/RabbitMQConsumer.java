package com.marketplace.rabbitmq.service;

import com.marketplace.api.dto.email.EmailRequest;
import com.marketplace.api.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.marketplace.rabbitmq.config.RabbitMQConfig.EMAIL_QUEUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = EMAIL_QUEUE)
    public void processEmail(EmailRequest emailRequest) {
        try {
            emailService.sendEmailAsync(emailRequest);
            log.info("Successfully processed email for: {}", emailRequest.to());
        } catch (Exception e) {
            log.error("Failed to process email for: {}", emailRequest.to(), e);
            // L'email sera automatiquement envoyé vers la DLQ
            throw e;
        }
    }

    @RabbitListener(queues = "email.dlq")
    public void processDLQ(EmailRequest emailRequest) {
        log.warn("Processing failed email from DLQ for: {}", emailRequest.to());
        try {
            // Logique de retry ou notification admin
            emailService.sendEmailSync(emailRequest); // Tentative synchrone
        } catch (Exception e) {
            log.error("Final failure processing email for: {}", emailRequest.to(), e);
            // Ici vous pourriez implémenter une logique de notification admin
        }
    }
}

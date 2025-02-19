package com.marketplace.rabbitmq.service.impl;

import com.marketplace.api.dto.email.EmailRequest;
import com.marketplace.api.service.RabbitQueueService;
import com.marketplace.rabbitmq.service.RabbitMQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.marketplace.rabbitmq.config.RabbitMQConfig.EMAIL_EXCHANGE;
import static com.marketplace.rabbitmq.config.RabbitMQConfig.ROUTING_KEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQProducer implements RabbitQueueService {
    private final RabbitMQService rabbitMQService;

    @Override
    public void queueEmail(EmailRequest emailRequest) {
        try {
            rabbitMQService.sendMessage(
                    EMAIL_EXCHANGE,
                    ROUTING_KEY,
                    emailRequest
            );
            log.atInfo().log("Email queued successfully for recipient: {}", emailRequest.to());
        } catch (Exception e) {
            log.atError().log("Failed to queue email for recipient: {}", emailRequest.to(), e);
            throw new RuntimeException("Failed to queue email", e);
        }
    }
}

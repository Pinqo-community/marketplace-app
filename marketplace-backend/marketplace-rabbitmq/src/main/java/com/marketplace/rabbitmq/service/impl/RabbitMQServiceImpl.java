package com.marketplace.rabbitmq.service.impl;

import com.marketplace.rabbitmq.service.RabbitMQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQServiceImpl implements RabbitMQService {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public <T> void sendMessage(String exchange, String routingKey, T message) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("Message sent to exchange: {}, routingKey: {}", exchange, routingKey);
        } catch (Exception e) {
            log.error("Failed to send message to RabbitMQ", e);
            throw new RuntimeException("Failed to send message", e);
        }
    }
}

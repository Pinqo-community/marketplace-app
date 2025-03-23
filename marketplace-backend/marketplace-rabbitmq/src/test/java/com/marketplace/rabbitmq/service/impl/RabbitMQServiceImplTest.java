package com.marketplace.rabbitmq.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMQServiceImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQServiceImpl rabbitMQService;

    @Test
    void should_send_message_successfully() {
        // Given
        String exchange = "test.exchange";
        String routingKey = "test.routing";
        String message = "test message";

        // When
        rabbitMQService.sendMessage(exchange, routingKey, message);

        // Then
        verify(rabbitTemplate).convertAndSend(exchange, routingKey, message);
    }

    @Test
    void should_throw_exception_when_send_fails() {
        // Given
        String exchange = "test.exchange";
        String routingKey = "test.routing";
        String message = "test message";

        doThrow(new AmqpException("Failed to send"))
                .when(rabbitTemplate)
                .convertAndSend(exchange, routingKey, message);

        // When & Then
        assertThrows(RuntimeException.class, () ->
                rabbitMQService.sendMessage(exchange, routingKey, message)
        );
    }
}
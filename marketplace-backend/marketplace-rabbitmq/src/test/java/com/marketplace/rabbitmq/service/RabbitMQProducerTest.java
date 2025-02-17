package com.marketplace.rabbitmq.service;

import com.marketplace.api.dto.email.EmailRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static com.marketplace.rabbitmq.config.RabbitMQConfig.EMAIL_EXCHANGE;
import static com.marketplace.rabbitmq.config.RabbitMQConfig.ROUTING_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMQProducerTest {

    @Mock
    private RabbitMQService rabbitMQService;

    @InjectMocks
    private RabbitMQProducer rabbitMQProducer;

    @Nested
    class QueueEmailTest {

        @Test
        void should_queue_email_successfully() {
            // Given
            EmailRequest emailRequest = EmailRequest.builder()
                    .templateName("welcome-template")
                    .to("test@email.com")
                    .subject("Welcome!")
                    .variables(new HashMap<>())
                    .build();

            // When
            rabbitMQProducer.queueEmail(emailRequest);

            // Then
            verify(rabbitMQService).sendMessage(
                    EMAIL_EXCHANGE,
                    ROUTING_KEY,
                    emailRequest
            );
        }

        @Test
        void should_throw_exception_when_queueing_fails() {
            // Given
            EmailRequest emailRequest = EmailRequest.builder()
                    .templateName("welcome-template")
                    .to("test@email.com")
                    .subject("Welcome!")
                    .variables(new HashMap<>())
                    .build();

            doThrow(new RuntimeException("Failed to send message"))
                    .when(rabbitMQService)
                    .sendMessage(EMAIL_EXCHANGE, ROUTING_KEY, emailRequest);

            // When & Then
            assertThrows(
                    RuntimeException.class,
                    () -> rabbitMQProducer.queueEmail(emailRequest),
                    "Failed to queue email"
            );
        }
    }
}
package com.marketplace.rabbitmq.service;

import com.marketplace.api.dto.email.EmailRequest;
import com.marketplace.api.service.EmailService;
import com.marketplace.rabbitmq.service.impl.RabbitMQConsumer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMQConsumerTest {

    private static final String TEST_EMAIL = "test@email.com";
    private static final String TEST_SUBJECT = "Test Subject";
    private static final String TEST_TEMPLATE = "test-template";

    @Mock
    private EmailService emailService;

    @InjectMocks
    private RabbitMQConsumer rabbitMQConsumer;

    @Nested
    class ProcessEmailTest {
        @Test
        void should_process_email_successfully() {
            // Given
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", "Test User");

            EmailRequest emailRequest = EmailRequest.builder()
                    .templateName(TEST_TEMPLATE)
                    .to(TEST_EMAIL)
                    .subject(TEST_SUBJECT)
                    .variables(variables)
                    .build();

            // When
            rabbitMQConsumer.processEmail(emailRequest);

            // Then
            verify(emailService).sendEmailAsync(emailRequest);
        }

        @Test
        void should_throw_exception_when_processing_email_fails() {
            // Given
            EmailRequest emailRequest = EmailRequest.builder()
                    .templateName(TEST_TEMPLATE)
                    .to(TEST_EMAIL)
                    .subject(TEST_SUBJECT)
                    .variables(new HashMap<>())
                    .build();

            doThrow(new RuntimeException("Failed to send email"))
                    .when(emailService)
                    .sendEmailAsync(emailRequest);

            // When & Then
            assertThrows(RuntimeException.class, () ->
                    rabbitMQConsumer.processEmail(emailRequest)
            );
        }
    }

    @Nested
    class ProcessDLQTest {
        @Test
        void should_process_dlq_email_successfully() {
            // Given
            Map<String, Object> variables = new HashMap<>();
            variables.put("resetLink", "http://example.com/reset");

            EmailRequest emailRequest = EmailRequest.builder()
                    .templateName(TEST_TEMPLATE)
                    .to(TEST_EMAIL)
                    .subject(TEST_SUBJECT)
                    .variables(variables)
                    .build();

            // When
            rabbitMQConsumer.processDLQ(emailRequest);

            // Then
            verify(emailService).sendEmailSync(emailRequest);
        }

        @Test
        void should_handle_dlq_email_failure_gracefully() {
            // Given
            EmailRequest emailRequest = EmailRequest.builder()
                    .templateName(TEST_TEMPLATE)
                    .to(TEST_EMAIL)
                    .subject(TEST_SUBJECT)
                    .variables(new HashMap<>())
                    .build();

            doThrow(new RuntimeException("Failed to send email"))
                    .when(emailService)
                    .sendEmailSync(emailRequest);

            // When
            rabbitMQConsumer.processDLQ(emailRequest);

            // Then
            verify(emailService).sendEmailSync(emailRequest);
        }
    }
}
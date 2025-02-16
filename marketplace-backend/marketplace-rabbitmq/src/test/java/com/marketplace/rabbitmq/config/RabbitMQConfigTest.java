package com.marketplace.rabbitmq.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQConfigTest {
    @Mock
    private ConnectionFactory connectionFactory;

    private final RabbitMQConfig config = new RabbitMQConfig();

    @Nested
    @DisplayName("Message Converter Tests")
    class MessageConverterTests {
        @Test
        @DisplayName("Should create Jackson2JsonMessageConverter")
        void shouldCreateJsonMessageConverter() {
            // When
            MessageConverter converter = config.jsonMessageConverter();

            // Then
            assertThat(converter)
                    .isNotNull()
                    .isInstanceOf(Jackson2JsonMessageConverter.class);
        }
    }

    @Nested
    @DisplayName("RabbitTemplate Tests")
    class RabbitTemplateTests {
        @Test
        @DisplayName("Should create RabbitTemplate with JSON converter")
        void shouldCreateRabbitTemplateWithJsonConverter() {
            // When
            RabbitTemplate template = config.rabbitTemplate(connectionFactory);

            // Then
            assertThat(template)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("connectionFactory", connectionFactory);

            assertThat(template.getMessageConverter())
                    .isNotNull()
                    .isInstanceOf(Jackson2JsonMessageConverter.class);
        }
    }
}
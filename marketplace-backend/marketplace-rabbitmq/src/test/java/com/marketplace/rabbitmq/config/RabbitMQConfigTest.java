package com.marketplace.rabbitmq.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
    class MessageConverterConfiguration {
        @Test
        void should_create_json_message_converter() {
            // When
            MessageConverter converter = config.jsonMessageConverter();

            // Then
            assertNotNull(converter);
            assertTrue(converter instanceof org.springframework.amqp.support.converter.Jackson2JsonMessageConverter);
        }

        @Test
        void should_configure_rabbit_template_with_json_converter() {
            // Given
            MessageConverter converter = config.jsonMessageConverter();

            // When
            RabbitTemplate template = config.rabbitTemplate(connectionFactory);

            // Then
            assertNotNull(template);
            assertEquals(connectionFactory, template.getConnectionFactory());
            assertTrue(template.getMessageConverter() instanceof org.springframework.amqp.support.converter.Jackson2JsonMessageConverter);
        }
    }

    @Nested
    class MainQueueConfiguration {
        @Test
        void should_create_email_queue_with_dlq_configuration() {
            // When
            Queue queue = config.emailQueue();

            // Then
            assertNotNull(queue);
            assertEquals(RabbitMQConfig.EMAIL_QUEUE, queue.getName());
            assertTrue(queue.isDurable());
            assertEquals("email.dlx", queue.getArguments().get("x-dead-letter-exchange"));
            assertEquals("email.dead", queue.getArguments().get("x-dead-letter-routing-key"));
        }

        @Test
        void should_create_email_exchange() {
            // When
            TopicExchange exchange = config.emailExchange();

            // Then
            assertNotNull(exchange);
            assertEquals(RabbitMQConfig.EMAIL_EXCHANGE, exchange.getName());
            assertTrue(exchange.isDurable());
            assertFalse(exchange.isAutoDelete());
        }

        @Test
        void should_bind_email_queue_to_exchange() {
            // Given
            Queue queue = config.emailQueue();
            TopicExchange exchange = config.emailExchange();

            // When
            Binding binding = config.emailBinding();

            // Then
            assertNotNull(binding);
            assertEquals(Binding.DestinationType.QUEUE, binding.getDestinationType());
            assertEquals(queue.getName(), binding.getDestination());
            assertEquals(exchange.getName(), binding.getExchange());
            assertEquals(RabbitMQConfig.ROUTING_KEY, binding.getRoutingKey());
        }
    }

    @Nested
    class DeadLetterQueueConfiguration {
        @Test
        void should_create_dead_letter_queue() {
            // When
            Queue dlq = config.deadLetterQueue();

            // Then
            assertNotNull(dlq);
            assertEquals("email.dlq", dlq.getName());
            assertTrue(dlq.isDurable());
        }

        @Test
        void should_create_dead_letter_exchange() {
            // When
            TopicExchange dlx = config.deadLetterExchange();

            // Then
            assertNotNull(dlx);
            assertEquals("email.dlx", dlx.getName());
            assertTrue(dlx.isDurable());
            assertFalse(dlx.isAutoDelete());
        }

        @Test
        void should_bind_dead_letter_queue_to_exchange() {
            // Given
            Queue dlq = config.deadLetterQueue();
            TopicExchange dlx = config.deadLetterExchange();

            // When
            Binding dlqBinding = config.deadLetterBinding();

            // Then
            assertNotNull(dlqBinding);
            assertEquals(Binding.DestinationType.QUEUE, dlqBinding.getDestinationType());
            assertEquals(dlq.getName(), dlqBinding.getDestination());
            assertEquals(dlx.getName(), dlqBinding.getExchange());
            assertEquals("email.dead", dlqBinding.getRoutingKey());
        }
    }

    @Nested
    class IntegrationTests {
        @Test
        void should_properly_configure_dead_letter_queue_integration() {
            // Given
            Queue mainQueue = config.emailQueue();
            Queue dlq = config.deadLetterQueue();
            TopicExchange dlx = config.deadLetterExchange();

            // When
            String configuredDlx = (String) mainQueue.getArguments().get("x-dead-letter-exchange");
            String configuredRoutingKey = (String) mainQueue.getArguments().get("x-dead-letter-routing-key");

            // Then
            assertEquals(dlx.getName(), configuredDlx, "DLX should match between main queue config and DLX");
            assertEquals("email.dead", configuredRoutingKey, "Routing key should match DLQ configuration");
            assertNotEquals(mainQueue.getName(), dlq.getName(), "Main queue and DLQ should have different names");
            assertTrue(dlq.getName().contains("dlq"), "DLQ should have 'dlq' in its name");
        }
    }
}
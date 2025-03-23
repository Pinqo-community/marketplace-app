package com.marketplace.rabbitmq.service;

public interface RabbitMQService {
    <T> void sendMessage(String exchange, String routingKey, T message);
}

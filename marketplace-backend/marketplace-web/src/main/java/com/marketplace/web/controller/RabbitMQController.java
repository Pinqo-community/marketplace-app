package com.marketplace.web.controller;

import com.marketplace.api.dto.email.EmailRequest;
import com.marketplace.rabbitmq.service.RabbitMQConsumer;
import com.marketplace.rabbitmq.service.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rabbitmq")
@RequiredArgsConstructor
@Slf4j
public class RabbitMQController {
    private final RabbitMQProducer producer;

    @GetMapping("/test/simple")
    public ResponseEntity<String> testSimpleEmail() {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("firstName", "Alice");
            variables.put("resetLink", "https://leetcode.com/");

            EmailRequest emailRequest = EmailRequest.builder()
                    .templateName("reset-password")
                    .to("user@example.com")
                    .subject("Réinitialisation de mot de passe")
                    .variables(variables)
                    .build();

            producer.queueEmail(emailRequest);

            return ResponseEntity.ok("Email queued successfully");
        } catch (Exception e) {
            log.error("Failed to queue test email", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to queue email: " + e.getMessage());
        }
    }
}

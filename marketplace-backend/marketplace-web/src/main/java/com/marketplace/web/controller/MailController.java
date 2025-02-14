package com.marketplace.web.controller;

import com.marketplace.api.dto.email.EmailRequest;
import com.marketplace.api.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
@Slf4j
public class MailController {
    private final EmailService emailService;

    @GetMapping
    public void createLabel() {
        try {
            log.atInfo().log("POST /mail - START");
            Map<String, Object> variables = new HashMap<>();
            variables.put("firstName", "Alice");
            variables.put("resetLink", "https://leetcode.com/");

            EmailRequest request = EmailRequest.builder()
                    .templateName("reset-password")
                    .to("user@example.com")
                    .subject("Réinitialisation de mot de passe")
                    .variables(variables)
                    .build();

            emailService.sendEmailAsync(request);
        } finally {
            log.atInfo().log("POST /mail - END");
        }
    }
}

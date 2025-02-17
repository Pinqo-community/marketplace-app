package com.marketplace.mail.service.impl;

import com.marketplace.api.dto.email.EmailRequest;
import com.marketplace.api.exception.EmailSendException;
import com.marketplace.api.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import jakarta.mail.MessagingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendEmailAsync(EmailRequest request) {
        sendEmail(request);
    }

    @Override
    public void sendEmailSync(EmailRequest request) {
        sendEmail(request);
    }

    public void sendEmail(EmailRequest request) {
        String htmlContent = buildEmailContent(request.templateName(), request.variables());
        MimeMessage message = buildMimeMessage(request, htmlContent);
        mailSender.send(message);
        log.info("Email sent successfully to: {}", request.to());
    }

    private String buildEmailContent(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        context.setVariable("contentTemplate", "emails/" + templateName);

        return templateEngine.process("emails/layout", context);
    }

    private MimeMessage buildMimeMessage(EmailRequest request, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("from@example.com");
            helper.setTo(request.to());
            helper.setSubject(request.subject());
            helper.setText(htmlContent, true);

            return message;
        } catch (MessagingException e) {
            log.error("Failed to build MimeMessage", e);
            throw new EmailSendException("Failed to build MimeMessage", e);
        }
    }
}

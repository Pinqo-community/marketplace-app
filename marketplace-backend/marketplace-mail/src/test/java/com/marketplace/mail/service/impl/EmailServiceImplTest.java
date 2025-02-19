package com.marketplace.mail.service.impl;

import com.marketplace.api.dto.email.EmailRequest;
import com.marketplace.api.exception.EmailSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    private static final String FROM_EMAIL = "from@example.com";

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Captor
    private ArgumentCaptor<Context> contextCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "from", FROM_EMAIL);
    }

    @Nested
    class SendEmailAsyncTest {
        @Test
        void should_call_send_email() {
            // Given
            EmailRequest request = createTestEmailRequest();
            EmailServiceImpl spyEmailService = spy(emailService);
            doNothing().when(spyEmailService).sendEmail(any(EmailRequest.class));

            // When
            spyEmailService.sendEmailAsync(request);

            // Then
            verify(spyEmailService).sendEmail(request);
        }
    }

    @Nested
    class SendEmailSyncTest {
        @Test
        void should_call_send_email() {
            // Given
            EmailRequest request = createTestEmailRequest();
            EmailServiceImpl spyEmailService = spy(emailService);
            doNothing().when(spyEmailService).sendEmail(any(EmailRequest.class));

            // When
            spyEmailService.sendEmailSync(request);

            // Then
            verify(spyEmailService).sendEmail(request);
        }
    }

    @Nested
    class SendEmailTest {
        @Test
        void should_send_email_successfully() {
            // Given
            EmailRequest request = createTestEmailRequest();
            when(templateEngine.process(anyString(), any(Context.class)))
                    .thenReturn("<html>Test content</html>");
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            // When
            emailService.sendEmail(request);

            // Then
            verify(mailSender).send(mimeMessage);
        }
    }

    @Nested
    class BuildEmailContentTest {
        @Test
        void should_build_email_content_successfully() {
            // Given
            EmailRequest request = createTestEmailRequest();
            when(templateEngine.process(anyString(), any(Context.class)))
                    .thenReturn("<html>Test content</html>");
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            // When
            emailService.sendEmail(request);

            // Then
            verify(templateEngine).process(eq("emails/layout"), contextCaptor.capture());
            Context capturedContext = contextCaptor.getValue();
            assertEquals("emails/" + request.templateName(), capturedContext.getVariable("contentTemplate"));
            Map<String, Object> variables = request.variables();
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                assertEquals(entry.getValue(), capturedContext.getVariable(entry.getKey()));
            }
        }
    }

    private EmailRequest createTestEmailRequest() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "Test User");

        return EmailRequest.builder()
                .templateName("test-template")
                .to("test@example.com")
                .subject("Test Subject")
                .variables(variables)
                .build();
    }
    @Nested
    class BuildMimeMessageTest {
        @Test
        void should_build_mime_message_successfully() {
            // Given
            EmailRequest request = createTestEmailRequest();
            when(templateEngine.process(anyString(), any(Context.class)))
                    .thenReturn("<html>Test content</html>");
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            // When
            emailService.sendEmail(request);

            // Then
            verify(mailSender).createMimeMessage();
        }

        @Test
        void should_throw_email_send_exception_when_build_fails() throws MessagingException {
            // Given
            EmailRequest request = createTestEmailRequest();
            MimeMessage mimeMessage = mock(MimeMessage.class);
            when(templateEngine.process(anyString(), any(Context.class)))
                    .thenReturn("<html>Test content</html>");
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doThrow(new MessagingException("Failed to create message"))
                    .when(mimeMessage).setContent(any(MimeMultipart.class));

            // When & Then
            assertThrows(EmailSendException.class, () ->
                    emailService.sendEmail(request)
            );
        }
    }
}
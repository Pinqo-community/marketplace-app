package com.marketplace.mail.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class MailConfigTest {

    private final MailConfig mailConfig = new MailConfig();

    @Test
    void should_configure_java_mail_sender_correctly() {
        // Given
        ReflectionTestUtils.setField(mailConfig, "username", "test-username");
        ReflectionTestUtils.setField(mailConfig, "password", "test-password");
        ReflectionTestUtils.setField(mailConfig, "host", "smtp.gmail.com");
        ReflectionTestUtils.setField(mailConfig, "port", 527);

        // When
        JavaMailSender mailSender = mailConfig.javaMailSender();

        // Then
        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;

        assertEquals("smtp.gmail.com", mailSenderImpl.getHost());
        assertEquals(527, mailSenderImpl.getPort());
        assertEquals("test-username", mailSenderImpl.getUsername());
        assertEquals("test-password", mailSenderImpl.getPassword());

        Properties props = mailSenderImpl.getJavaMailProperties();
        assertTrue(Boolean.parseBoolean(props.getProperty("mail.smtp.auth")));
        assertTrue(Boolean.parseBoolean(props.getProperty("mail.smtp.starttls.enable")));
    }
}
package com.marketplace.mail.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.mail.username=test-username",
        "spring.mail.password=test-password"
})
class MailConfigTest {

    private final MailConfig mailConfig = new MailConfig();

    @Test
    void should_configure_java_mail_sender_correctly() {
        // Given
        ReflectionTestUtils.setField(mailConfig, "username", "test-username");
        ReflectionTestUtils.setField(mailConfig, "password", "test-password");

        // When
        JavaMailSender mailSender = mailConfig.javaMailSender();

        // Then
        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;

        assertEquals("sandbox.smtp.mailtrap.io", mailSenderImpl.getHost());
        assertEquals(2525, mailSenderImpl.getPort());
        assertEquals("test-username", mailSenderImpl.getUsername());
        assertEquals("test-password", mailSenderImpl.getPassword());

        Properties props = mailSenderImpl.getJavaMailProperties();
        assertTrue(Boolean.parseBoolean(props.getProperty("mail.smtp.auth")));
        assertTrue(Boolean.parseBoolean(props.getProperty("mail.smtp.starttls.enable")));
    }
}
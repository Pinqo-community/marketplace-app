package com.marketplace.api.service;

import com.marketplace.api.dto.email.EmailRequest;

public interface EmailService {
    void sendEmailAsync(EmailRequest request);
    void sendEmailSync(EmailRequest request);
}

package com.marketplace.api.dto.email;

import java.util.HashMap;
import java.util.Map;

public record EmailRequest(
    String templateName,
    String to,
    String subject,
    Map<String, Object> variables
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String templateName;
        private String to;
        private String subject;
        private Map<String, Object> variables = new HashMap<>();

        // Builder methods...
        public Builder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder variables(Map<String, Object> variables) {
            this.variables = variables;
            return this;
        }

        public EmailRequest build() {
            return new EmailRequest(templateName, to, subject, variables);
        }
    }
}

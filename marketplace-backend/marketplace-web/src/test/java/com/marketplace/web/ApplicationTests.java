package com.marketplace.web;

import com.marketplace.web.configuration.TestCorsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestCorsConfig.class)
class ApplicationTests {

    @Test
    void contextLoads() {
    }

}

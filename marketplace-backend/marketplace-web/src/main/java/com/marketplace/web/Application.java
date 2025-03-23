package com.marketplace.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.marketplace.web",
        "com.marketplace.auth",
        "com.marketplace.core",
        "com.marketplace.mail",
        "com.marketplace.rabbitmq",
        "com.marketplace.storage",
        "com.marketplace.api",
})
@EntityScan(basePackages = {"com.marketplace.core.entity", "com.marketplace.auth.entity"})
@EnableJpaRepositories(basePackages = {"com.marketplace.core.repository", "com.marketplace.auth.repository"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

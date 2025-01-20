package com.marketplace.core.configuration;

import com.marketplace.core.entity.Role;
import com.marketplace.core.entity.enums.RoleType;
import com.marketplace.core.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        Arrays.stream(RoleType.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(role));
            }
        });
    }
}
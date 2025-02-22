package com.marketplace.core.configuration;

import com.marketplace.core.entity.Role;
import com.marketplace.core.entity.enums.RoleType;
import com.marketplace.core.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Database initializer component.
 * Responsible for populating initial role data in the database on application startup.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    /**
     * Executes the data initialization process.
     * Creates default roles in the database if they don't already exist.
     *
     * @param args command line arguments passed to the application
     */
    @Override
    public void run(String... args) {
        Arrays.stream(RoleType.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(role));
            }
        });
    }
}
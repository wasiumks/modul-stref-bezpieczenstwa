package com.maciejwasiak.locon.config;

import com.maciejwasiak.locon.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final AuthService authService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing default users...");
        authService.createDefaultUsers();
        log.info("Default users initialized successfully");
    }
}

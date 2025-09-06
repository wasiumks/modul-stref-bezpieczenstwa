package com.maciejwasiak.locon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MockController {

    // Mock endpoints that don't conflict with the new API implementations
    // The following endpoints are now handled by proper controllers:
    // - /api/zones -> ZoneApiController
    // - /api/devices -> DeviceApiController  
    // - /api/user/permissions -> UserApiController
    // - /api/themes/{operator} -> ThemeApiController
    // - /api/i18n/{lang} -> I18nApiController

    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "message", "Mock controller is running");
    }
}
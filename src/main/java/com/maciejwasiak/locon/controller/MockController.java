package com.maciejwasiak.locon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MockController {

    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "message", "Mock controller is running");
    }
}
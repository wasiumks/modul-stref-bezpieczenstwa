package com.maciejwasiak.locon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MockController {

    @GetMapping("/api/zones")
    public List<Map<String, Object>> zones() {
        return List.of(
                Map.of("id", 1, "name", "Zone A"),
                Map.of("id", 2, "name", "Zone B")
        );
    }

    @GetMapping("/api/devices")
    public List<Map<String, Object>> devices() {
        return List.of(
                Map.of("id", 1, "name", "Device 1"),
                Map.of("id", 2, "name", "Device 2")
        );
    }

    @GetMapping("/api/user/permissions")
    public Map<String, Boolean> permissions() {
        return Map.of("canEdit", true, "canDelete", false);
    }

    @GetMapping("/api/themes/{operator}")
    public Map<String, String> themes(@PathVariable String operator) {
        return Map.of("operator", operator, "theme", "dark");
    }

    @GetMapping("/api/i18n/{lang}")
    public Map<String, String> i18n(@PathVariable String lang) {
        return Map.of(
                "hello", lang.equals("pl") ? "Witaj Świecie!" : "Hello World!"
        );
    }
}
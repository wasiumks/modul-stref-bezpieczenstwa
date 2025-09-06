package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.dto.ThemeDto;
import com.maciejwasiak.locon.service.ThemeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
@Slf4j
public class ThemeApiController {
    
    private final ThemeService themeService;
    
    @GetMapping("/{operator}")
    public ResponseEntity<Map<String, String>> getTheme(@PathVariable String operator) {
        log.info("GET /api/themes/{} - Fetching theme for operator", operator);
        Map<String, String> theme = themeService.getThemeAsMap(operator);
        return ResponseEntity.ok(theme);
    }
    
    @GetMapping("/{operator}/dto")
    public ResponseEntity<ThemeDto> getThemeDto(@PathVariable String operator) {
        log.info("GET /api/themes/{}/dto - Fetching theme DTO for operator", operator);
        ThemeDto theme = themeService.getThemeForOperator(operator);
        return ResponseEntity.ok(theme);
    }
}

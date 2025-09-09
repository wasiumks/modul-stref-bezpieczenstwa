package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.service.I18nService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/i18n")
@RequiredArgsConstructor
@Slf4j
public class I18nApiController {
    
    private final I18nService i18nService;
    
    @GetMapping("/{lang}")
    public ResponseEntity<Map<String, String>> getTranslations(@PathVariable String lang) {
        log.debug("Getting translations for language: {}", lang);
        Map<String, String> translations = i18nService.getTranslations(lang);
        log.debug("Retrieved {} translations for language: {}", translations.size(), lang);
        return ResponseEntity.ok(translations);
    }
}

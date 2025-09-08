package com.maciejwasiak.locon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class I18nService {
    
    private final MessageSource messageSource;
    
    public Map<String, String> getTranslations(String language) {
        log.debug("Fetching translations for language");
        
        Locale locale = getLocaleFromLanguage(language);
        Map<String, String> translations = new HashMap<>();
        
        // Define all the message keys that should be included in the translation package
        String[] messageKeys = {
            "hello", "zones", "back", "add_zone", "add_first_zone", "add_another", "go_to_zones",
            "zone_name", "zone_address", "zone_radius", "zone_icon", "zone_notifications",
            "zone_created", "zone_created_description",
            "step_1_of_4", "step_2_of_4", "step_3_of_4", "step_4_of_4",
            "next", "save", "cancel",
            "zone_name_placeholder", "zone_address_placeholder", "choose_icon",
            "set_location", "set_zone_area", "choose_devices", "radius_range",
            "current_radius", "adjust_radius",
            "zones_empty_title", "zones_empty_description", "zones_empty_benefit1", "zones_empty_benefit2",
            "device_phone", "device_watch", "device_band", "enable_notifications", "notification_tip"
        };
        
        for (String key : messageKeys) {
            try {
                String translation = messageSource.getMessage(key, null, locale);
                translations.put(key, translation);
            } catch (Exception e) {
                log.debug("Translation not found for key: {} in language: {}", key, language);
                translations.put(key, key); // Fallback to key if translation not found
            }
        }
        
        return translations;
    }
    
    private Locale getLocaleFromLanguage(String language) {
        return switch (language.toLowerCase()) {
            case "pl" -> Locale.forLanguageTag("pl-PL");
            case "de" -> Locale.forLanguageTag("de-DE");
            case "en" -> Locale.forLanguageTag("en-US");
            default -> Locale.forLanguageTag("en-US"); // Default to English
        };
    }
}

package com.maciejwasiak.locon.service;

import com.maciejwasiak.locon.dto.ThemeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemeService {
    
    public ThemeDto getThemeForOperator(String operator) {
        log.debug("Fetching theme for operator");
        
        // Mock implementation - in real app this would fetch from database or external service
        return switch (operator.toLowerCase()) {
            case "play" -> ThemeDto.createDefaultTheme("Play");
            case "plus" -> ThemeDto.createDefaultTheme("Plus");
            case "orange" -> ThemeDto.createDarkTheme("Orange");
            case "t-mobile" -> ThemeDto.createDefaultTheme("T-Mobile");
            default -> ThemeDto.createDefaultTheme(operator);
        };
    }
    
    public Map<String, String> getThemeAsMap(String operator) {
        return getThemeForOperator(operator).toMap();
    }
}

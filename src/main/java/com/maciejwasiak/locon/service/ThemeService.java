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
            case "play" -> createPlayTheme();
            case "plus" -> createPlusTheme();
            case "orange" -> createOrangeTheme();
            case "t-mobile" -> createTMobileTheme();
            default -> ThemeDto.createDefaultTheme(operator);
        };
    }
    
    public Map<String, String> getThemeAsMap(String operator) {
        return getThemeForOperator(operator).toMap();
    }

    private ThemeDto createPlayTheme() {
        // Example brand-like colors (not official)
        return new ThemeDto(
            "Play",
            "#5A2A82", // primary (purple)
            "#7C3AED", // accent
            "#EF4444", // error
            "#F9FAFB", // background
            "#111827", // text
            "#E5E7EB", // border
            "rgba(0,0,0,0.15)" // shadow
        );
    }

    private ThemeDto createPlusTheme() {
        return new ThemeDto(
            "Plus",
            "#00A651", // primary (green)
            "#34D399", // accent
            "#F87171", // error
            "#F7FEE7", // background (very light green)
            "#064E3B", // text (deep green)
            "#D1FAE5", // border
            "rgba(0,0,0,0.15)"
        );
    }

    private ThemeDto createOrangeTheme() {
        return new ThemeDto(
            "Orange",
            "#FF7900", // primary (orange)
            "#FDBA74", // accent
            "#DC2626", // error
            "#FFF7ED", // background (warm)
            "#1F2937", // text
            "#FDE68A", // border
            "rgba(0,0,0,0.15)"
        );
    }

    private ThemeDto createTMobileTheme() {
        return new ThemeDto(
            "T-Mobile",
            "#E20074", // primary (magenta)
            "#F472B6", // accent
            "#FB7185", // error
            "#FDF2F8", // background (rose-50)
            "#111827", // text
            "#FBCFE8", // border
            "rgba(0,0,0,0.15)"
        );
    }
}

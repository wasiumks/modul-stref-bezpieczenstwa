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
        log.debug("Fetching theme for operator: {}", operator);
        return switch (operator.toLowerCase()) {
            case "play" -> createPlayTheme();
            case "plus" -> createPlusTheme();
            case "orange" -> createOrangeTheme();
            case "t-mobile" -> createTMobileTheme();
            default -> ThemeDto.createDefaultTheme(operator);
        };
    }
    
    public Map<String, String> getThemeAsMap(String operator) {
        log.debug("Getting theme as map for operator: {}", operator);
        return getThemeForOperator(operator).toMap();
    }

    private ThemeDto createPlayTheme() {
        return new ThemeDto(
            "Play",
            "#5A2A82",
            "#7C3AED",
            "#EF4444",
            "#F9FAFB",
            "#111827",
            "#E5E7EB",
            "rgba(0,0,0,0.15)"
        );
    }

    private ThemeDto createPlusTheme() {
        return new ThemeDto(
            "Plus",
            "#00A651",
            "#34D399",
            "#F87171",
            "#F7FEE7",
            "#064E3B",
            "#D1FAE5",
            "rgba(0,0,0,0.15)"
        );
    }

    private ThemeDto createOrangeTheme() {
        return new ThemeDto(
            "Orange",
            "#FF7900",
            "#FDBA74",
            "#DC2626",
            "#FFF7ED",
            "#1F2937",
            "#FDE68A",
            "rgba(0,0,0,0.15)"
        );
    }

    private ThemeDto createTMobileTheme() {
        return new ThemeDto(
            "T-Mobile",
            "#E20074",
            "#F472B6",
            "#FB7185",
            "#FDF2F8",
            "#111827",
            "#FBCFE8",
            "rgba(0,0,0,0.15)"
        );
    }
}

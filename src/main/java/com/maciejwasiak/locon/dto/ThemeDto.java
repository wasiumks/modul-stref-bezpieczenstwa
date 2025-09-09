package com.maciejwasiak.locon.dto;

import java.util.Map;

public record ThemeDto(
    String operator,
    String primaryColor,
    String accentColor,
    String errorColor,
    String backgroundColor,
    String textColor,
    String borderColor,
    String shadowColor
) {
    public static ThemeDto createDefaultTheme(String operator) {
        return new ThemeDto(
            operator,
            "#2C5282",
            "#50C878",
            "#FF6B6B",
            "#F5F5F5",
            "#1A202C",
            "#E2E8F0",
            "#000000"
        );
    }
    
    public static ThemeDto createDarkTheme(String operator) {
        return new ThemeDto(
            operator,
            "#4299E1",
            "#68D391",
            "#FC8181",
            "#1A202C",
            "#F7FAFC",
            "#4A5568",
            "#000000"
        );
    }
    
    public Map<String, String> toMap() {
        return Map.of(
            "operator", operator,
            "primaryColor", primaryColor,
            "accentColor", accentColor,
            "errorColor", errorColor,
            "backgroundColor", backgroundColor,
            "textColor", textColor,
            "borderColor", borderColor,
            "shadowColor", shadowColor
        );
    }
}

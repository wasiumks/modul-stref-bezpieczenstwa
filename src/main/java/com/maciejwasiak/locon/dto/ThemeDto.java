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
            "#2C5282",  // primary
            "#50C878",  // accent
            "#FF6B6B",  // error
            "#F5F5F5",  // background
            "#1A202C",  // text
            "#E2E8F0",  // border
            "#000000"   // shadow
        );
    }
    
    public static ThemeDto createDarkTheme(String operator) {
        return new ThemeDto(
            operator,
            "#4299E1",  // primary
            "#68D391",  // accent
            "#FC8181",  // error
            "#1A202C",  // background
            "#F7FAFC",  // text
            "#4A5568",  // border
            "#000000"   // shadow
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

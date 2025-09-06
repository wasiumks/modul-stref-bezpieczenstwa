package com.maciejwasiak.locon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ZonesController {

    @GetMapping("/zones")
    public String zones(Model model) {
        // Mock data for zones - this will be replaced with actual service calls later
        List<Map<String, Object>> zones = List.of(
                Map.of(
                        "id", 1,
                        "name", "Dom",
                        "address", "ul. Przykładowa 123, Warszawa",
                        "icon", "home",
                        "devicesCount", 2,
                        "radius", 500
                ),
                Map.of(
                        "id", 2,
                        "name", "Szkoła",
                        "address", "ul. Szkolna 45, Warszawa",
                        "icon", "school",
                        "devicesCount", 1,
                        "radius", 300
                )
        );

        model.addAttribute("pageTitle", "Strefy Bezpieczeństwa");
        model.addAttribute("pageDescription", "Zarządzaj strefami bezpieczeństwa dla swoich bliskich");
        model.addAttribute("zones", zones);
        model.addAttribute("hasZones", !zones.isEmpty());

        return "zones";
    }
}

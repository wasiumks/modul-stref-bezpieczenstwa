package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class ZonesController {

    @GetMapping("/zones")
    public String zones(Model model, HttpSession session) {
        // Get user from session
        User user = (User) session.getAttribute("user");
        System.out.println("=== ZONES CONTROLLER ===");
        System.out.println("Session ID: " + session.getId());
        System.out.println("User in session: " + (user != null ? user.getPhone() + " - " + user.getRole() : "null"));
        System.out.println("All session attributes: " + java.util.Collections.list(session.getAttributeNames()));
        
        if (user == null) {
            System.out.println("No user in session, redirecting to login");
            return "redirect:/auth/login";
        }
        
        // For Phase 5.2 - Show sample zones for list view demonstration
        List<Map<String, Object>> zones = List.of(
                Map.of(
                        "id", 1,
                        "name", "Dom",
                        "address", "ul. Przykładowa 123, Warszawa",
                        "icon", "home",
                        "devicesCount", 2,
                        "radius", 500,
                        "devices", List.of("iPhone 13", "Apple Watch")
                ),
                Map.of(
                        "id", 2,
                        "name", "Szkoła",
                        "address", "ul. Szkolna 45, Warszawa",
                        "icon", "school",
                        "devicesCount", 1,
                        "radius", 300,
                        "devices", List.of("Samsung Galaxy")
                ),
                Map.of(
                        "id", 3,
                        "name", "Praca",
                        "address", "ul. Biurowa 78, Warszawa",
                        "icon", "work",
                        "devicesCount", 1,
                        "radius", 200,
                        "devices", List.of("iPhone 13")
                )
        );

        model.addAttribute("pageTitle", "Strefy Bezpieczeństwa");
        model.addAttribute("pageDescription", "Zarządzaj strefami bezpieczeństwa dla swoich bliskich");
        model.addAttribute("zones", zones);
        model.addAttribute("hasZones", !zones.isEmpty());
        model.addAttribute("user", user);

        return "zones";
    }

    @GetMapping("/zones/wizard")
    public String zoneWizard(@RequestParam(defaultValue = "0") int step, Model model, HttpSession session) {
        // Get user from session
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Wizard steps configuration
        List<Map<String, String>> wizardSteps = List.of(
                Map.of("title", "Nazwa i ikona", "description", "Wybierz nazwę i ikonę strefy"),
                Map.of("title", "Lokalizacja", "description", "Ustaw lokalizację strefy"),
                Map.of("title", "Promień", "description", "Określ promień strefy"),
                Map.of("title", "Powiadomienia", "description", "Wybierz urządzenia do powiadomień")
        );

        // Available icons
        List<Map<String, String>> availableIcons = List.of(
                Map.of("name", "home", "emoji", "🏠", "label", "Dom"),
                Map.of("name", "school", "emoji", "🏫", "label", "Szkoła"),
                Map.of("name", "work", "emoji", "🏢", "label", "Praca"),
                Map.of("name", "hospital", "emoji", "🏥", "label", "Szpital"),
                Map.of("name", "gym", "emoji", "💪", "label", "Siłownia"),
                Map.of("name", "park", "emoji", "🌳", "label", "Park"),
                Map.of("name", "shop", "emoji", "🛒", "label", "Sklep"),
                Map.of("name", "restaurant", "emoji", "🍽️", "label", "Restauracja")
        );

        // Mock user devices
        List<Map<String, Object>> userDevices = List.of(
                Map.of("id", 1, "name", "iPhone 13", "type", "Telefon"),
                Map.of("id", 2, "name", "Apple Watch", "type", "Smartwatch"),
                Map.of("id", 3, "name", "Samsung Galaxy", "type", "Telefon")
        );

        model.addAttribute("pageTitle", "Kreator strefy");
        model.addAttribute("currentStep", step);
        model.addAttribute("wizardSteps", wizardSteps);
        model.addAttribute("availableIcons", availableIcons);
        model.addAttribute("userDevices", userDevices);
        model.addAttribute("user", user);

        return "zone-wizard";
    }
}

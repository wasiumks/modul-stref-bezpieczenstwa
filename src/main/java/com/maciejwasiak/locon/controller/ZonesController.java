package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        model.addAttribute("user", user);

        return "zones-simple";
    }
}

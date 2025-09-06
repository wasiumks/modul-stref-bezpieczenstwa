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
        
        // For Phase 5.1 - Start with empty state to demonstrate onboarding
        List<Map<String, Object>> zones = List.of(); // Empty list for onboarding

        model.addAttribute("pageTitle", "Strefy Bezpieczeństwa");
        model.addAttribute("pageDescription", "Zarządzaj strefami bezpieczeństwa dla swoich bliskich");
        model.addAttribute("zones", zones);
        model.addAttribute("hasZones", !zones.isEmpty());
        model.addAttribute("user", user);

        return "zones";
    }
}

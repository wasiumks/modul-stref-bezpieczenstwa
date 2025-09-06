package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.Zone;
import com.maciejwasiak.locon.service.ZoneService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class ZonesController {
    
    @Autowired
    private ZoneService zoneService;

    @GetMapping("/zones")
    public String zones(@RequestParam(required = false) String deleted, Model model, HttpSession session) {
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
        
        // Get zones from database
        List<Zone> zones = zoneService.getZonesByUser(user);
        
        // Get statistics
        long totalZones = zoneService.getZoneCountByUser(user);
        int totalDevices = zoneService.getTotalDeviceCountByUser(user);

        model.addAttribute("pageTitle", "Strefy Bezpieczeństwa");
        model.addAttribute("pageDescription", "Zarządzaj strefami bezpieczeństwa dla swoich bliskich");
        model.addAttribute("zones", zones);
        model.addAttribute("hasZones", !zones.isEmpty());
        model.addAttribute("user", user);
        model.addAttribute("showDeletedMessage", "true".equals(deleted));
        model.addAttribute("totalZones", totalZones);
        model.addAttribute("totalDevices", totalDevices);

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

    @GetMapping("/zones/success")
    public String zoneSuccess(@RequestParam Long zoneId, Model model, HttpSession session) {
        // Get user from session
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Get zone from database
        Zone zone = zoneService.getZoneByIdAndUser(zoneId, user)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        // Get statistics
        long totalZones = zoneService.getZoneCountByUser(user);
        int totalDevices = zoneService.getTotalDeviceCountByUser(user);
        
        // Debug logging
        System.out.println("=== ZONE SUCCESS DEBUG ===");
        System.out.println("zoneId: " + zoneId);
        System.out.println("zone: " + zone.getName() + " - " + zone.getAddress());

        model.addAttribute("pageTitle", "Strefa utworzona");
        model.addAttribute("zone", zone);
        model.addAttribute("totalZones", totalZones);
        model.addAttribute("totalDevices", totalDevices);
        model.addAttribute("user", user);

        return "zone-success";
    }

    @PostMapping("/zones")
    public String createZone(@RequestParam String name, 
                           @RequestParam String icon, 
                           @RequestParam String address, 
                           @RequestParam int radius, 
                           @RequestParam(required = false) String[] deviceIds,
                           HttpSession session) {
        // Get user from session
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Create zone in database
        System.out.println("=== CREATING ZONE ===");
        System.out.println("Name: " + name);
        System.out.println("Icon: " + icon);
        System.out.println("Address: " + address);
        System.out.println("Radius: " + radius);
        System.out.println("Device IDs: " + (deviceIds != null ? String.join(",", deviceIds) : "none"));
        
        // Convert deviceIds array to List
        List<String> deviceIdList = deviceIds != null ? Arrays.asList(deviceIds) : List.of();
        
        // Create zone in database
        Zone createdZone = zoneService.createZone(name, address, icon, radius, deviceIdList, user);
        
        // Redirect to success page with zone ID
        return "redirect:/zones/success?zoneId=" + createdZone.getId();
    }

    @GetMapping("/zones/edit/{id}")
    public String editZone(@PathVariable int id, Model model, HttpSession session) {
        // Get user from session
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        // For now, redirect to wizard with edit mode
        // In a real implementation, this would load existing zone data
        System.out.println("=== EDITING ZONE ===");
        System.out.println("Zone ID: " + id);
        
        return "redirect:/zones/wizard?step=0&edit=true&id=" + id;
    }

    @PostMapping("/zones/delete/{id}")
    public String deleteZone(@PathVariable int id, HttpSession session) {
        // Get user from session
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Delete zone from database
        System.out.println("=== DELETING ZONE ===");
        System.out.println("Zone ID: " + id);
        
        try {
            zoneService.deleteZone((long) id, user);
            System.out.println("Zone deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting zone: " + e.getMessage());
        }
        
        return "redirect:/zones?deleted=true";
    }
}

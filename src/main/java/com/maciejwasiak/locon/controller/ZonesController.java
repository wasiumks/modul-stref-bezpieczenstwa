package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.UserRole;
import com.maciejwasiak.locon.model.Zone;
import com.maciejwasiak.locon.service.ZoneService;
import com.maciejwasiak.locon.service.DevicePermissionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class ZonesController {
    
    @Autowired
    private ZoneService zoneService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private DevicePermissionService devicePermissionService;

    @GetMapping("/zones")
    public String zones(@RequestParam(required = false) String deleted, 
                       @RequestParam(required = false) String error,
                       @RequestParam(required = false) String message,
                       Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        List<Zone> zones = zoneService.getZonesByUser(user);
        long totalZones = zoneService.getZoneCountByUser(user);
        int totalDevices = zoneService.getTotalDeviceCountByUser(user);
        
        Map<String, String> deviceIdToName = Map.of(
            "1", "Phone SOS",
            "2", "GJD.13 Watch", 
            "3", "BS.07 Band"
        );

        model.addAttribute("pageTitle", messageSource.getMessage("zones_title", null, LocaleContextHolder.getLocale()));
        model.addAttribute("pageDescription", "Zarządzaj strefami bezpieczeństwa dla swoich bliskich");
        model.addAttribute("zones", zones);
        model.addAttribute("hasZones", !zones.isEmpty());
        model.addAttribute("user", user);
        model.addAttribute("userRole", user.getRole().name());
        model.addAttribute("showDeletedMessage", "true".equals(deleted));
        model.addAttribute("showError", "true".equals(error));
        model.addAttribute("errorMessage", message);
        model.addAttribute("totalZones", totalZones);
        model.addAttribute("totalDevices", totalDevices);
        model.addAttribute("deviceIdToName", deviceIdToName);

        return "zones";
    }

    public String zones(String view, Model model, HttpSession session) {
        try {
            String sid = session.getId();
            java.util.Enumeration<String> names = session.getAttributeNames();
            if (names != null) {
                while (names.hasMoreElements()) {
                    names.nextElement();
                }
            }
        } catch (Exception ignored) {}

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("pageTitle", "Strefy Bezpieczeństwa");
        model.addAttribute("pageDescription", "Zarządzaj strefami bezpieczeństwa dla swoich bliskich");
        model.addAttribute("user", user);

        java.util.List<java.util.Map<String, Object>> zones = java.util.List.of(
                java.util.Map.of(
                        "name", "Dom",
                        "address", "ul. Przykładowa 123, Warszawa",
                        "icon", "home",
                        "devicesCount", 2,
                        "radius", 500
                ),
                java.util.Map.of(
                        "name", "Szkoła",
                        "address", "ul. Szkolna 45, Warszawa",
                        "icon", "school",
                        "devicesCount", 1,
                        "radius", 300
                )
        );
        model.addAttribute("zones", zones);
        model.addAttribute("hasZones", true);

        return "zones-simple";
    }

    @GetMapping("/zones/wizard")
    public String zoneWizard(@RequestParam(defaultValue = "0") int step, 
                            @RequestParam(required = false) Boolean edit,
                            @RequestParam(required = false) Long id,
                            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (user.getRole() == UserRole.VIEWER) {
            return "redirect:/zones?error=true&message=Brak%20uprawnie%C5%84";
        }
        List<Map<String, String>> wizardSteps = List.of(
                Map.of("title", "Nazwa i ikona", "description", "Wybierz nazwę i ikonę strefy"),
                Map.of("title", "Lokalizacja", "description", "Ustaw lokalizację strefy"),
                Map.of("title", "Promień", "description", "Określ promień strefy"),
                Map.of("title", "Powiadomienia", "description", "Wybierz urządzenia do powiadomień")
        );

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

        // Get devices based on user permissions
        List<Map<String, Object>> userDevices = devicePermissionService.getAvailableDevices(user.getRole());

        Zone existingZone = null;
        if (Boolean.TRUE.equals(edit) && id != null) {
            existingZone = zoneService.getZoneByIdAndUser(id, user)
                    .orElseThrow(() -> new RuntimeException("Zone not found"));
        }

        String pageTitle = Boolean.TRUE.equals(edit)
                ? messageSource.getMessage("wizard_edit_title", null, LocaleContextHolder.getLocale())
                : messageSource.getMessage("wizard_title", null, LocaleContextHolder.getLocale());
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("currentStep", step);
        model.addAttribute("wizardSteps", wizardSteps);
        model.addAttribute("availableIcons", availableIcons);
        model.addAttribute("userDevices", userDevices);
        model.addAttribute("user", user);
        model.addAttribute("isEditMode", Boolean.TRUE.equals(edit));
        model.addAttribute("existingZone", existingZone);

        String mapsApiKey = System.getenv().getOrDefault("GOOGLE_MAPS_API_KEY", "");
        model.addAttribute("googleMapsApiKey", mapsApiKey);

        return "zone-wizard";
    }

    @GetMapping("/zones/success")
    public String zoneSuccess(@RequestParam Long zoneId, 
                            @RequestParam(required = false) Boolean updated,
                            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        Zone zone = zoneService.getZoneByIdAndUser(zoneId, user)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        long totalZones = zoneService.getZoneCountByUser(user);
        int totalDevices = zoneService.getTotalDeviceCountByUser(user);
        

        model.addAttribute("pageTitle", Boolean.TRUE.equals(updated) ? "Strefa zaktualizowana" : "Strefa utworzona");
        model.addAttribute("zone", zone);
        model.addAttribute("totalZones", totalZones);
        model.addAttribute("totalDevices", totalDevices);
        model.addAttribute("user", user);
        model.addAttribute("updated", Boolean.TRUE.equals(updated));

        return "zone-success";
    }

    @PostMapping("/zones")
    public String createZone(@RequestParam String name, 
                           @RequestParam String icon, 
                           @RequestParam String address, 
                           @RequestParam int radius,
                           @RequestParam(required = false) Double latitude,
                           @RequestParam(required = false) Double longitude,
                           @RequestParam(required = false) String[] deviceIds,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (user.getRole() == UserRole.VIEWER) {
            return "redirect:/zones?error=true&message=Brak%20uprawnie%C5%84";
        }
        
        List<String> deviceIdList = deviceIds != null ? new ArrayList<>(Arrays.asList(deviceIds)) : new ArrayList<>();
        
        Zone createdZone = zoneService.createZone(name, address, icon, radius, latitude, longitude, deviceIdList, user);
        
        return "redirect:/zones/success?zoneId=" + createdZone.getId();
    }

    @PostMapping("/zones/update/{id}")
    public String updateZone(@PathVariable Long id,
                           @RequestParam String name, 
                           @RequestParam String icon, 
                           @RequestParam String address, 
                           @RequestParam int radius,
                           @RequestParam(required = false) Double latitude,
                           @RequestParam(required = false) Double longitude,
                           @RequestParam(required = false) String[] deviceIds,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (user.getRole() == UserRole.VIEWER) {
            return "redirect:/zones?error=true&message=Brak%20uprawnie%C5%84";
        }
        
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa strefy jest wymagana");
        }
        if (icon == null || icon.trim().isEmpty()) {
            throw new IllegalArgumentException("Ikona strefy jest wymagana");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Adres strefy jest wymagany");
        }
        
        List<String> deviceIdList = deviceIds != null ? new ArrayList<>(Arrays.asList(deviceIds)) : new ArrayList<>();
        
        try {
            Zone updatedZone = zoneService.updateZone(id, name, address, icon, radius, latitude, longitude, deviceIdList, user);
            
            return "redirect:/zones/success?zoneId=" + updatedZone.getId() + "&updated=true";
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                errorMessage = "Nieznany błąd podczas aktualizacji strefy: " + e.getClass().getSimpleName();
            }
            
            try {
                errorMessage = java.net.URLEncoder.encode(errorMessage, "UTF-8");
            } catch (Exception encodingException) {
                errorMessage = "Błąd podczas aktualizacji strefy";
            }
            
            return "redirect:/zones?error=update_failed&message=" + errorMessage;
        }
    }

    @GetMapping("/zones/edit/{id}")
    public String editZone(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        Zone zone = zoneService.getZoneByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        return "redirect:/zones/wizard?step=0&edit=true&id=" + id;
    }

    @PostMapping("/zones/delete/{id}")
    public String deleteZone(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (user.getRole() == UserRole.VIEWER) {
            return "redirect:/zones?error=true&message=Brak%20uprawnie%C5%84";
        }
        
        try {
            zoneService.deleteZone((long) id, user);
        } catch (Exception e) {
            // Handle error silently
        }
        
        return "redirect:/zones?deleted=true";
    }
}

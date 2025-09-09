package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.UserRole;
import com.maciejwasiak.locon.service.DevicePermissionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    
    private final DevicePermissionService devicePermissionService;
    
    @GetMapping("/devices")
    public String deviceManagement(@RequestParam(required = false) String message,
                                  Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        if (user.getRole() != UserRole.ADMIN) {
            log.warn("Non-admin user {} attempted to access admin device management", user.getPhone());
            return "redirect:/zones?error=true&message=Brak%20uprawnie%C5%84";
        }
        
        // Get device permissions from service
        Map<String, Map<String, Boolean>> devicePermissions = devicePermissionService.getAllDevicePermissions();
        
        List<Map<String, Object>> devices = List.of(
            Map.of("id", 1, "name", "Phone SOS", "type", "Telefon", "permissions", devicePermissions.get("Phone SOS")),
            Map.of("id", 2, "name", "GJD.13 Watch", "type", "Smartwatch", "permissions", devicePermissions.get("GJD.13 Watch")),
            Map.of("id", 3, "name", "BS.07 Band", "type", "Opaska", "permissions", devicePermissions.get("BS.07 Band"))
        );
        
        List<Map<String, String>> userRoles = List.of(
            Map.of("role", "ADMIN", "name", "Administrator", "description", "Pełny dostęp do wszystkich funkcji"),
            Map.of("role", "USER", "name", "Użytkownik Standard", "description", "Dostęp do wybranych urządzeń"),
            Map.of("role", "VIEWER", "name", "Przeglądający", "description", "Dostęp tylko do odczytu")
        );
        
        model.addAttribute("pageTitle", "Zarządzanie urządzeniami");
        model.addAttribute("pageDescription", "Konfiguruj uprawnienia dostępu do urządzeń dla różnych ról użytkowników");
        model.addAttribute("devices", devices);
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("user", user);
        model.addAttribute("showSuccessMessage", "success".equals(message));
        model.addAttribute("successMessage", "Uprawnienia zostały zaktualizowane");
        
        log.debug("Admin {} accessed device management page", user.getPhone());
        return "admin/device-management";
    }
    
    @PostMapping("/devices/permissions")
    public String updateDevicePermissions(@RequestParam Map<String, String> allParams,
                                        HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        if (user.getRole() != UserRole.ADMIN) {
            log.warn("Non-admin user {} attempted to update device permissions", user.getPhone());
            return "redirect:/zones?error=true&message=Brak%20uprawnie%C5%84";
        }
        
        // Process permission updates
        // Get all device IDs and roles to check both enabled and disabled permissions
        String[] deviceIds = {"1", "2", "3"};
        String[] roles = {"USER", "VIEWER"};
        
        for (String deviceId : deviceIds) {
            for (String role : roles) {
                String paramName = "device_" + deviceId + "_permission_" + role;
                boolean enabled = allParams.containsKey(paramName) && "true".equals(allParams.get(paramName));
                
                String deviceName = getDeviceNameById(deviceId);
                if (deviceName != null) {
                    devicePermissionService.updateDevicePermission(deviceName, role, enabled);
                    log.info("Admin {} updated device {} permission for role {} to {}", 
                        user.getPhone(), deviceName, role, enabled);
                }
            }
        }
        
        log.info("Admin {} updated device permissions successfully", user.getPhone());
        return "redirect:/admin/devices?message=success";
    }
    
    private String getDeviceNameById(String deviceId) {
        return switch (deviceId) {
            case "1" -> "Phone SOS";
            case "2" -> "GJD.13 Watch";
            case "3" -> "BS.07 Band";
            default -> null;
        };
    }
}

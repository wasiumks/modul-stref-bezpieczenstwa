package com.maciejwasiak.locon.service;

import com.maciejwasiak.locon.model.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DevicePermissionService {
    
    // Device permissions configuration
    // This would typically come from database, but for now we'll use static configuration
    private static Map<String, Map<String, Boolean>> DEVICE_PERMISSIONS = new java.util.HashMap<>(Map.of(
        "Phone SOS", new java.util.HashMap<>(Map.of(
            "ADMIN", true,
            "USER", true,
            "VIEWER", false
        )),
        "GJD.13 Watch", new java.util.HashMap<>(Map.of(
            "ADMIN", true,
            "USER", true,
            "VIEWER", false
        )),
        "BS.07 Band", new java.util.HashMap<>(Map.of(
            "ADMIN", true,
            "USER", false,
            "VIEWER", false
        ))
    ));
    
    public List<Map<String, Object>> getAvailableDevices(UserRole userRole) {
        log.debug("Getting available devices for role: {}", userRole);
        
        List<Map<String, Object>> allDevices = List.of(
            Map.of("id", 1, "name", "Phone SOS", "type", "Telefon"),
            Map.of("id", 2, "name", "GJD.13 Watch", "type", "Smartwatch"),
            Map.of("id", 3, "name", "BS.07 Band", "type", "Opaska")
        );
        
        List<Map<String, Object>> availableDevices = allDevices.stream()
            .filter(device -> hasDevicePermission(device, userRole))
            .toList();
        
        log.debug("Found {} available devices for role: {}", availableDevices.size(), userRole);
        return availableDevices;
    }
    
    public boolean hasDevicePermission(Map<String, Object> device, UserRole userRole) {
        String deviceName = (String) device.get("name");
        Map<String, Boolean> permissions = DEVICE_PERMISSIONS.get(deviceName);
        
        if (permissions == null) {
            log.warn("No permissions found for device: {}", deviceName);
            return false;
        }
        
        Boolean hasPermission = permissions.get(userRole.name());
        boolean result = hasPermission != null && hasPermission;
        
        log.debug("Device {} permission for role {}: {}", deviceName, userRole, result);
        return result;
    }
    
    public Map<String, Map<String, Boolean>> getAllDevicePermissions() {
        log.debug("Getting all device permissions");
        return DEVICE_PERMISSIONS;
    }
    
    public void updateDevicePermission(String deviceName, String role, boolean enabled) {
        log.info("Updating device permission: {} for role {} to {}", deviceName, role, enabled);
        
        Map<String, Boolean> devicePermissions = DEVICE_PERMISSIONS.get(deviceName);
        if (devicePermissions != null) {
            devicePermissions.put(role, enabled);
            log.info("Device permission updated successfully: {} for role {} is now {}", deviceName, role, enabled);
        } else {
            log.warn("Device not found: {}", deviceName);
        }
    }
}

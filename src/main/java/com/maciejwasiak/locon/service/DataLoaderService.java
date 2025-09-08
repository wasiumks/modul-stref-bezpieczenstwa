package com.maciejwasiak.locon.service;

import com.maciejwasiak.locon.model.*;
import com.maciejwasiak.locon.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class DataLoaderService implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final ZoneRepository zoneRepository;
    private final ZoneDeviceRepository zoneDeviceRepository;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.debug("Starting data loading...");
        
        // Check if data already exists
        if (userRepository.count() > 0) {
            log.debug("Data already exists, skipping data loading");
            return;
        }
        
        // Create sample users
        User admin = createUser("+48123456789", UserRole.ADMIN);
        User user = createUser("+48987654321", UserRole.USER);
        User viewer = createUser("+48111222333", UserRole.VIEWER);
        
        // Create sample devices
        Device adminPhone = createDevice("Phone", "iPhone 15", "Admin User", admin);
        Device userPhone = createDevice("Phone", "Samsung Galaxy S24", "Regular User", user);
        Device childWatch = createDevice("Child Watch", "KidsGuard Pro", "Emma (Child)", user);
        Device seniorBand = createDevice("Senior Band", "HealthTracker 3000", "John (Senior)", user);
        createDevice("Phone", "Google Pixel 8", "Viewer User", viewer);
        
        // Create sample zones
        Zone homeZone = createZone("Home", "🏠", "123 Main Street, Warsaw, Poland", 52.2297, 21.0122, 100);
        Zone schoolZone = createZone("School", "🏫", "456 Education Ave, Warsaw, Poland", 52.2370, 21.0175, 200);
        
        // Assign devices to zones
        createZoneDevice(homeZone, adminPhone, true);
        createZoneDevice(homeZone, userPhone, true);
        createZoneDevice(homeZone, childWatch, true);
        createZoneDevice(homeZone, seniorBand, true);
        
        createZoneDevice(schoolZone, childWatch, true);
        createZoneDevice(schoolZone, userPhone, false); // Notifications disabled for school
        
        log.debug("Data loading completed successfully!");
        log.debug("Created {} users, {} devices, {} zones, {} zone-device assignments", 
                userRepository.count(), deviceRepository.count(), zoneRepository.count(), zoneDeviceRepository.count());
    }
    
    private User createUser(String phone, UserRole role) {
        User user = new User(phone, role);
        return userRepository.save(user);
    }
    
    private Device createDevice(String type, String model, String ownerName, User user) {
        Device device = new Device(type, model, ownerName, user);
        return deviceRepository.save(device);
    }
    
    private Zone createZone(String name, String icon, String address, Double latitude, Double longitude, Integer radius) {
        Zone zone = new Zone(name, icon, address, latitude, longitude, radius);
        return zoneRepository.save(zone);
    }
    
    private ZoneDevice createZoneDevice(Zone zone, Device device, Boolean notificationsEnabled) {
        ZoneDevice zoneDevice = new ZoneDevice(zone, device, notificationsEnabled);
        return zoneDeviceRepository.save(zoneDevice);
    }
}

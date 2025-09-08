package com.maciejwasiak.locon.service;

import com.maciejwasiak.locon.dto.ZoneDto;
import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.Zone;
import com.maciejwasiak.locon.repository.ZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ZoneService {
    
    private static final Logger log = LoggerFactory.getLogger(ZoneService.class);
    
    @Autowired
    private ZoneRepository zoneRepository;
    
    /**
     * Create a new zone for a user
     */
    public Zone createZone(String name, String address, String icon, Integer radius, 
                          List<String> deviceIds, User user) {
        log.info("Creating zone: {} for user: {}", name, user.getPhone());
        log.info("User ID: {}, User Phone: {}", user.getId(), user.getPhone());
        log.info("Device IDs: {}", deviceIds);
        
        try {
            Zone zone = new Zone(name, address, icon, radius, user);
            zone.setDeviceIds(deviceIds);
            
            log.info("Zone object created, saving to database...");
            Zone savedZone = zoneRepository.save(zone);
            log.info("Zone created successfully with ID: {}", savedZone.getId());
            
            return savedZone;
        } catch (Exception e) {
            log.error("Error creating zone: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Create a new zone with coordinates
     */
    public Zone createZone(String name, String icon, String address, Double latitude, 
                          Double longitude, Integer radius) {
        log.info("Creating zone: {} at coordinates: {}, {}", name, latitude, longitude);
        
        Zone zone = new Zone(name, icon, address, latitude, longitude, radius);
        
        Zone savedZone = zoneRepository.save(zone);
        log.info("Zone created successfully with ID: {}", savedZone.getId());
        
        return savedZone;
    }
    
    /**
     * Get all zones (for API)
     */
    @Transactional(readOnly = true)
    public List<Zone> getAllZones() {
        log.info("Fetching all zones");
        return zoneRepository.findAll();
    }
    
    /**
     * Get zone by ID (for API)
     */
    @Transactional(readOnly = true)
    public Optional<Zone> getZoneById(Long zoneId) {
        log.info("Fetching zone ID: {}", zoneId);
        return zoneRepository.findById(zoneId);
    }
    
    /**
     * Get all zones for a user
     */
    @Transactional(readOnly = true)
    public List<Zone> getZonesByUser(User user) {
        log.info("Fetching zones for user: {}", user.getPhone());
        return zoneRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    /**
     * Get a specific zone by ID for a user
     */
    @Transactional(readOnly = true)
    public Optional<Zone> getZoneByIdAndUser(Long zoneId, User user) {
        log.info("Fetching zone ID: {} for user: {}", zoneId, user.getPhone());
        return zoneRepository.findByIdAndUser(zoneId, user);
    }
    
    /**
     * Update an existing zone
     */
    public Zone updateZone(Long zoneId, String name, String address, String icon, 
                          Integer radius, List<String> deviceIds, User user) {
        log.info("Updating zone ID: {} for user: {}", zoneId, user.getPhone());
        
        Zone zone = zoneRepository.findByIdAndUser(zoneId, user)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        zone.setName(name);
        zone.setAddress(address);
        zone.setIcon(icon);
        zone.setRadius(radius);
        zone.setDeviceIds(deviceIds);
        
        Zone updatedZone = zoneRepository.save(zone);
        log.info("Zone updated successfully with ID: {}", updatedZone.getId());
        
        return updatedZone;
    }
    
    /**
     * Update an existing zone (for API)
     */
    public Zone updateZone(Long zoneId, ZoneDto zoneDto) {
        log.info("Updating zone ID: {} via API", zoneId);
        
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        zone.setName(zoneDto.name());
        zone.setIcon(zoneDto.icon());
        zone.setAddress(zoneDto.address());
        zone.setLatitude(zoneDto.latitude());
        zone.setLongitude(zoneDto.longitude());
        zone.setRadius(zoneDto.radius());
        
        Zone updatedZone = zoneRepository.save(zone);
        log.info("Zone updated successfully with ID: {}", updatedZone.getId());
        
        return updatedZone;
    }
    
    /**
     * Delete a zone
     */
    public void deleteZone(Long zoneId, User user) {
        log.info("Deleting zone ID: {} for user: {}", zoneId, user.getPhone());
        
        if (!zoneRepository.existsByIdAndUser(zoneId, user)) {
            throw new RuntimeException("Zone not found");
        }
        
        zoneRepository.deleteByIdAndUser(zoneId, user);
        log.info("Zone deleted successfully with ID: {}", zoneId);
    }
    
    /**
     * Delete a zone (for API)
     */
    public void deleteZone(Long zoneId) {
        log.info("Deleting zone ID: {} via API", zoneId);
        
        if (!zoneRepository.existsById(zoneId)) {
            throw new RuntimeException("Zone not found");
        }
        
        zoneRepository.deleteById(zoneId);
        log.info("Zone deleted successfully with ID: {}", zoneId);
    }
    
    /**
     * Get total zone count for a user
     */
    @Transactional(readOnly = true)
    public long getZoneCountByUser(User user) {
        return zoneRepository.countByUser(user);
    }
    
    /**
     * Get total device count across all zones for a user
     */
    @Transactional(readOnly = true)
    public int getTotalDeviceCountByUser(User user) {
        List<Zone> zones = zoneRepository.findByUserOrderByCreatedAtDesc(user);
        return zones.stream()
                .mapToInt(Zone::getDeviceCount)
                .sum();
    }
    
    /**
     * Toggle notifications for a zone
     */
    public Zone toggleNotifications(Long zoneId, Boolean enabled) {
        log.info("Toggling notifications for zone ID: {} to: {}", zoneId, enabled);
        
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        zone.setNotificationsEnabled(enabled);
        Zone updatedZone = zoneRepository.save(zone);
        log.info("Notifications toggled successfully for zone ID: {}", zoneId);
        
        return updatedZone;
    }
}
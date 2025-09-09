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
    
    public Zone createZone(String name, String address, String icon, Integer radius, 
                          List<String> deviceIds, User user) {
        log.debug("Creating zone '{}' for user: {}", name, user.getPhone());
        
        try {
            Zone zone = new Zone(name, address, icon, radius, user);
            zone.setDeviceIds(deviceIds);
            
            Zone savedZone = zoneRepository.save(zone);
            log.info("Zone '{}' created successfully with ID: {} for user: {}", name, savedZone.getId(), user.getPhone());
            
            return savedZone;
        } catch (Exception e) {
            log.error("Error creating zone '{}' for user: {}", name, user.getPhone(), e);
            throw e;
        }
    }

    public Zone createZone(String name, String address, String icon, Integer radius,
                           Double latitude, Double longitude,
                           List<String> deviceIds, User user) {
        log.debug("Creating zone '{}' with coordinates for user: {}", name, user.getPhone());

        try {
            Zone zone = new Zone(name, address, icon, radius, user);
            if (latitude != null) {
                zone.setLatitude(latitude);
            }
            if (longitude != null) {
                zone.setLongitude(longitude);
            }
            zone.setDeviceIds(deviceIds);

            Zone savedZone = zoneRepository.save(zone);
            log.info("Zone '{}' with coordinates created successfully with ID: {} for user: {}", name, savedZone.getId(), user.getPhone());
            return savedZone;
        } catch (Exception e) {
            log.error("Error creating zone '{}' with coordinates for user: {}", name, user.getPhone(), e);
            throw e;
        }
    }
    
    public Zone createZone(String name, String icon, String address, Double latitude, 
                          Double longitude, Integer radius) {
        log.debug("Creating zone '{}' with coordinates", name);
        
        Zone zone = new Zone(name, icon, address, latitude, longitude, radius);
        
        Zone savedZone = zoneRepository.save(zone);
        log.info("Zone '{}' created successfully with ID: {}", name, savedZone.getId());
        
        return savedZone;
    }
    
    @Transactional(readOnly = true)
    public List<Zone> getAllZones() {
        log.debug("Fetching all zones");
        List<Zone> zones = zoneRepository.findAll();
        log.debug("Retrieved {} zones", zones.size());
        return zones;
    }
    
    @Transactional(readOnly = true)
    public Optional<Zone> getZoneById(Long zoneId) {
        log.debug("Fetching zone by ID: {}", zoneId);
        return zoneRepository.findById(zoneId);
    }
    
    @Transactional(readOnly = true)
    public List<Zone> getZonesByUser(User user) {
        log.debug("Fetching zones for user: {}", user.getPhone());
        List<Zone> zones = zoneRepository.findByUserOrderByCreatedAtDesc(user);
        log.debug("Retrieved {} zones for user: {}", zones.size(), user.getPhone());
        return zones;
    }
    
    @Transactional(readOnly = true)
    public Optional<Zone> getZoneByIdAndUser(Long zoneId, User user) {
        log.debug("Fetching zone by ID: {} for user: {}", zoneId, user.getPhone());
        return zoneRepository.findByIdAndUser(zoneId, user);
    }
    
    public Zone updateZone(Long zoneId, String name, String address, String icon, 
                          Integer radius, List<String> deviceIds, User user) {
        log.debug("Updating zone ID: {} '{}' for user: {}", zoneId, name, user.getPhone());
        
        Zone zone = zoneRepository.findByIdAndUser(zoneId, user)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        zone.setName(name);
        zone.setAddress(address);
        zone.setIcon(icon);
        zone.setRadius(radius);
        zone.setDeviceIds(deviceIds);
        
        Zone updatedZone = zoneRepository.save(zone);
        log.info("Zone '{}' updated successfully with ID: {} for user: {}", name, updatedZone.getId(), user.getPhone());
        
        return updatedZone;
    }

    public Zone updateZone(Long zoneId, String name, String address, String icon,
                           Integer radius, Double latitude, Double longitude,
                           List<String> deviceIds, User user) {
        log.debug("Updating zone ID: {} '{}' with coordinates for user: {}", zoneId, name, user.getPhone());

        Zone zone = zoneRepository.findByIdAndUser(zoneId, user)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        zone.setName(name);
        zone.setAddress(address);
        zone.setIcon(icon);
        zone.setRadius(radius);
        if (latitude != null) {
            zone.setLatitude(latitude);
        }
        if (longitude != null) {
            zone.setLongitude(longitude);
        }
        zone.setDeviceIds(deviceIds);

        Zone updatedZone = zoneRepository.save(zone);
        log.info("Zone '{}' with coordinates updated successfully with ID: {} for user: {}", name, updatedZone.getId(), user.getPhone());

        return updatedZone;
    }
    
    public Zone updateZone(Long zoneId, ZoneDto zoneDto) {
        log.debug("Updating zone via API with ID: {}", zoneId);
        
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        zone.setName(zoneDto.name());
        zone.setIcon(zoneDto.icon());
        zone.setAddress(zoneDto.address());
        zone.setLatitude(zoneDto.latitude());
        zone.setLongitude(zoneDto.longitude());
        zone.setRadius(zoneDto.radius());
        
        Zone updatedZone = zoneRepository.save(zone);
        log.info("Zone '{}' updated via API successfully with ID: {}", zoneDto.name(), updatedZone.getId());
        
        return updatedZone;
    }
    
    public void deleteZone(Long zoneId, User user) {
        log.debug("Deleting zone ID: {} for user: {}", zoneId, user.getPhone());
        
        if (!zoneRepository.existsByIdAndUser(zoneId, user)) {
            throw new RuntimeException("Zone not found");
        }
        
        zoneRepository.deleteByIdAndUser(zoneId, user);
        log.info("Zone ID: {} deleted successfully for user: {}", zoneId, user.getPhone());
    }
    
    public void deleteZone(Long zoneId) {
        log.debug("Deleting zone via API with ID: {}", zoneId);
        
        if (!zoneRepository.existsById(zoneId)) {
            throw new RuntimeException("Zone not found");
        }
        
        zoneRepository.deleteById(zoneId);
        log.info("Zone ID: {} deleted via API successfully", zoneId);
    }
    
    @Transactional(readOnly = true)
    public long getZoneCountByUser(User user) {
        log.debug("Getting zone count for user: {}", user.getPhone());
        long count = zoneRepository.countByUser(user);
        log.debug("User {} has {} zones", user.getPhone(), count);
        return count;
    }
    
    @Transactional(readOnly = true)
    public int getTotalDeviceCountByUser(User user) {
        log.debug("Getting total device count for user: {}", user.getPhone());
        List<Zone> zones = zoneRepository.findByUserOrderByCreatedAtDesc(user);
        int totalDevices = zones.stream()
                .mapToInt(Zone::getDeviceCount)
                .sum();
        log.debug("User {} has {} total devices across all zones", user.getPhone(), totalDevices);
        return totalDevices;
    }
    
    public Zone toggleNotifications(Long zoneId, Boolean enabled) {
        log.debug("Toggling notifications for zone ID: {} to: {}", zoneId, enabled);
        
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        zone.setNotificationsEnabled(enabled);
        Zone updatedZone = zoneRepository.save(zone);
        log.info("Notifications toggled to {} for zone ID: {}", enabled, zoneId);
        
        return updatedZone;
    }
}
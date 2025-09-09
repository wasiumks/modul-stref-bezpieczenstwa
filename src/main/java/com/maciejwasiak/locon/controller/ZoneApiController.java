package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.dto.ZoneCreateRequest;
import com.maciejwasiak.locon.dto.ZoneDto;
import com.maciejwasiak.locon.model.Zone;
import com.maciejwasiak.locon.service.ZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@Slf4j
public class ZoneApiController {
    
    private final ZoneService zoneService;
    
    @GetMapping
    public ResponseEntity<List<ZoneDto>> getAllZones() {
        log.debug("Getting all zones");
        List<Zone> zones = zoneService.getAllZones();
        List<ZoneDto> zoneDtos = zones.stream()
                .map(ZoneDto::from)
                .collect(Collectors.toList());
        log.debug("Retrieved {} zones", zoneDtos.size());
        return ResponseEntity.ok(zoneDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ZoneDto> getZoneById(@PathVariable Long id) {
        log.debug("Getting zone by id: {}", id);
        return zoneService.getZoneById(id)
                .map(zone -> {
                    log.debug("Zone found: {}", zone.getName());
                    return ResponseEntity.ok(ZoneDto.from(zone));
                })
                .orElseGet(() -> {
                    log.warn("Zone not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @PostMapping
    public ResponseEntity<ZoneDto> createZone(@Valid @RequestBody ZoneCreateRequest request) {
        log.debug("Creating new zone: {}", request.name());
        ZoneDto zoneDto = request.toZoneDto();
        Zone createdZone = zoneService.createZone(zoneDto.name(), zoneDto.icon(), zoneDto.address(), 
                zoneDto.latitude(), zoneDto.longitude(), zoneDto.radius());
        log.info("Zone created successfully with id: {}", createdZone.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ZoneDto.from(createdZone));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ZoneDto> updateZone(@PathVariable Long id, @Valid @RequestBody ZoneCreateRequest request) {
        log.debug("Updating zone with id: {}", id);
        ZoneDto zoneDto = request.toZoneDto();
        Zone updatedZone = zoneService.updateZone(id, zoneDto);
        log.info("Zone updated successfully with id: {}", id);
        return ResponseEntity.ok(ZoneDto.from(updatedZone));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        log.debug("Deleting zone with id: {}", id);
        try {
            zoneService.deleteZone(id);
            log.info("Zone deleted successfully with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Zone not found for deletion with id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/notifications")
    public ResponseEntity<ZoneDto> toggleNotifications(@PathVariable Long id, 
                                                     @RequestParam Boolean enabled) {
        log.debug("Toggling notifications for zone id: {} to: {}", id, enabled);
        try {
            Zone updatedZone = zoneService.toggleNotifications(id, enabled);
            log.info("Notifications toggled for zone id: {} to: {}", id, enabled);
            return ResponseEntity.ok(ZoneDto.from(updatedZone));
        } catch (RuntimeException e) {
            log.warn("Zone not found for notification toggle with id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}

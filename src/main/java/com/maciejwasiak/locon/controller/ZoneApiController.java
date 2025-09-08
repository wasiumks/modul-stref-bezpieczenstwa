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
        log.debug("GET /api/zones");
        List<Zone> zones = zoneService.getAllZones();
        List<ZoneDto> zoneDtos = zones.stream()
                .map(ZoneDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(zoneDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ZoneDto> getZoneById(@PathVariable Long id) {
        log.debug("GET /api/zones/{}", id);
        return zoneService.getZoneById(id)
                .map(ZoneDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ZoneDto> createZone(@Valid @RequestBody ZoneCreateRequest request) {
        log.debug("POST /api/zones");
        ZoneDto zoneDto = request.toZoneDto();
        Zone createdZone = zoneService.createZone(zoneDto.name(), zoneDto.icon(), zoneDto.address(), 
                zoneDto.latitude(), zoneDto.longitude(), zoneDto.radius());
        return ResponseEntity.status(HttpStatus.CREATED).body(ZoneDto.from(createdZone));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ZoneDto> updateZone(@PathVariable Long id, @Valid @RequestBody ZoneCreateRequest request) {
        log.debug("PUT /api/zones/{}", id);
        ZoneDto zoneDto = request.toZoneDto();
        Zone updatedZone = zoneService.updateZone(id, zoneDto);
        return ResponseEntity.ok(ZoneDto.from(updatedZone));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        log.debug("DELETE /api/zones/{}", id);
        try {
            zoneService.deleteZone(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/notifications")
    public ResponseEntity<ZoneDto> toggleNotifications(@PathVariable Long id, 
                                                     @RequestParam Boolean enabled) {
        log.debug("PUT /api/zones/{}/notifications", id);
        try {
            Zone updatedZone = zoneService.toggleNotifications(id, enabled);
            return ResponseEntity.ok(ZoneDto.from(updatedZone));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

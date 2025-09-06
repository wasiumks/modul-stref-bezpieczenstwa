package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.dto.ZoneCreateRequest;
import com.maciejwasiak.locon.dto.ZoneDto;
import com.maciejwasiak.locon.service.ZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@Slf4j
public class ZoneApiController {
    
    private final ZoneService zoneService;
    
    @GetMapping
    public ResponseEntity<List<ZoneDto>> getAllZones() {
        log.info("GET /api/zones - Fetching all zones");
        List<ZoneDto> zones = zoneService.getAllZones();
        return ResponseEntity.ok(zones);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ZoneDto> getZoneById(@PathVariable Long id) {
        log.info("GET /api/zones/{} - Fetching zone by id", id);
        return zoneService.getZoneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ZoneDto> createZone(@Valid @RequestBody ZoneCreateRequest request) {
        log.info("POST /api/zones - Creating new zone: {}", request.name());
        ZoneDto zoneDto = request.toZoneDto();
        ZoneDto createdZone = zoneService.createZone(zoneDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdZone);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ZoneDto> updateZone(@PathVariable Long id, @Valid @RequestBody ZoneCreateRequest request) {
        log.info("PUT /api/zones/{} - Updating zone: {}", id, request.name());
        ZoneDto zoneDto = request.toZoneDto();
        return zoneService.updateZone(id, zoneDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        log.info("DELETE /api/zones/{} - Deleting zone", id);
        boolean deleted = zoneService.deleteZone(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

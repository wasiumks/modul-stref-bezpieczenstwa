package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.dto.GeofenceEventDto;
import com.maciejwasiak.locon.service.GeofencingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/geofence")
public class GeofencingController {

    private static final Logger log = LoggerFactory.getLogger(GeofencingController.class);

    private final GeofencingService geofencingService;

    public GeofencingController(GeofencingService geofencingService) {
        this.geofencingService = geofencingService;
    }

    @GetMapping("/events/{zoneId}")
    public ResponseEntity<List<GeofenceEventDto>> getMockEvents(
        @PathVariable Long zoneId,
        @RequestParam(name = "count", required = false, defaultValue = "10") int count
    ) {
        log.info("Simulating geofence events: zoneId={}, count={}", zoneId, count);
        List<GeofenceEventDto> events = geofencingService.generateMockEvents(zoneId, count);
        for (GeofenceEventDto e : events) {
            log.info("Geofence {}: zone='{}' device='{}' at [{}, {}] radius={}m time={}",
                e.eventType(), e.zoneName(), e.deviceName(),
                String.format("%.5f", e.latitude()), String.format("%.5f", e.longitude()),
                e.radiusMeters(), e.occurredAt());
        }
        return ResponseEntity.ok(events);
    }
}



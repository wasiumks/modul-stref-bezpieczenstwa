package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.dto.GeofenceEventDto;
import com.maciejwasiak.locon.service.GeofencingService;
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

    private final GeofencingService geofencingService;

    public GeofencingController(GeofencingService geofencingService) {
        this.geofencingService = geofencingService;
    }

    @GetMapping("/events/{zoneId}")
    public ResponseEntity<List<GeofenceEventDto>> getMockEvents(
        @PathVariable Long zoneId,
        @RequestParam(name = "count", required = false, defaultValue = "10") int count
    ) {
        List<GeofenceEventDto> events = geofencingService.generateMockEvents(zoneId, count);
        return ResponseEntity.ok(events);
    }
}



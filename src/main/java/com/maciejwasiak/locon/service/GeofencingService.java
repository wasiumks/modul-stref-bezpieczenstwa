package com.maciejwasiak.locon.service;

import com.maciejwasiak.locon.dto.GeofenceEventDto;
import com.maciejwasiak.locon.model.Zone;
import com.maciejwasiak.locon.repository.ZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class GeofencingService {

    private static final Logger log = LoggerFactory.getLogger(GeofencingService.class);
    private final ZoneRepository zoneRepository;
    private final Random random = new Random();

    public GeofencingService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<GeofenceEventDto> generateMockEvents(Long zoneId, int count) {
        log.debug("Generating {} mock events for zone: {}", count, zoneId);
        Zone zone = zoneRepository.findById(zoneId)
            .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

        List<GeofenceEventDto> events = new ArrayList<>();
        List<String> deviceIds = zone.getDeviceIds() != null ? zone.getDeviceIds() : List.of();
        if (deviceIds.isEmpty()) {
            deviceIds = List.of("virtual-1");
            log.debug("No devices assigned to zone, using virtual device");
        }

        for (int i = 0; i < Math.max(1, count); i++) {
            String deviceId = deviceIds.get(random.nextInt(deviceIds.size()));
            String deviceName = mockDeviceName(deviceId);
            String eventType = random.nextBoolean() ? "ENTER" : "EXIT";
            double[] jitter = jitterCoordinates(zone.getLatitude(), zone.getLongitude(), zone.getRadius());

            events.add(new GeofenceEventDto(
                zone.getId(),
                zone.getName(),
                deviceId,
                deviceName,
                jitter[0],
                jitter[1],
                zone.getRadius() != null ? zone.getRadius() : 500,
                eventType,
                Instant.now().minusSeconds(random.nextInt(3600))
            ));
        }

        log.debug("Generated {} mock geofence events for zone {}", events.size(), zoneId);
        return events;
    }

    private String mockDeviceName(String deviceId) {
        return switch (deviceId) {
            case "1" -> "Phone SOS";
            case "2" -> "GJD.13 Watch";
            case "3" -> "BS.07 Band";
            default -> "Device " + deviceId;
        };
    }

    private double[] jitterCoordinates(Double lat, Double lng, Integer radiusMeters) {
        if (lat == null || lng == null) {
            lat = 52.2297;
            lng = 21.0122;
        }
        double maxOffsetMeters = Math.min(50, radiusMeters != null ? radiusMeters / 5.0 : 50);
        double dLat = (random.nextDouble() - 0.5) * (maxOffsetMeters / 111_000.0) * 2;
        double dLng = (random.nextDouble() - 0.5) * (maxOffsetMeters / (111_320.0 * Math.cos(Math.toRadians(lat)))) * 2;
        return new double[] { lat + dLat, lng + dLng };
    }
}



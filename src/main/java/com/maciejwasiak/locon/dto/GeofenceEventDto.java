package com.maciejwasiak.locon.dto;

import java.time.Instant;

/**
 * DTO representing a mock geofencing event for a device and zone.
 */
public record GeofenceEventDto(
    Long zoneId,
    String zoneName,
    String deviceId,
    String deviceName,
    double latitude,
    double longitude,
    int radiusMeters,
    String eventType, // ENTER or EXIT
    Instant occurredAt
) {}



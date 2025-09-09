package com.maciejwasiak.locon.dto;

import java.time.Instant;

public record GeofenceEventDto(
    Long zoneId,
    String zoneName,
    String deviceId,
    String deviceName,
    double latitude,
    double longitude,
    int radiusMeters,
    String eventType,
    Instant occurredAt
) {}



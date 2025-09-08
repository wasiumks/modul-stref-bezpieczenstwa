package com.maciejwasiak.locon.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZoneCreateRequestTest {

    @Test
    void toZoneDto_ShouldMapFieldsAndDefaultNotificationsEnabled() {
        // Given
        String name = "Home";
        String icon = "home";
        String address = "123 Main St";
        Double latitude = 52.2297;
        Double longitude = 21.0122;
        Integer radius = 150;

        ZoneCreateRequest req = new ZoneCreateRequest(
                name, icon, address, latitude, longitude, radius
        );

        // When
        ZoneDto dto = req.toZoneDto();

        // Then
        assertNull(dto.id());
        assertEquals(name, dto.name());
        assertEquals(icon, dto.icon());
        assertEquals(address, dto.address());
        assertEquals(latitude, dto.latitude());
        assertEquals(longitude, dto.longitude());
        assertEquals(radius, dto.radius());
        assertTrue(dto.notificationsEnabled());
    }
}



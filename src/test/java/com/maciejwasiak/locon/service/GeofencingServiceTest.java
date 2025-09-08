package com.maciejwasiak.locon.service;

import com.maciejwasiak.locon.dto.GeofenceEventDto;
import com.maciejwasiak.locon.model.Zone;
import com.maciejwasiak.locon.repository.ZoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeofencingServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private GeofencingService geofencingService;

    @Test
    void generateMockEvents_WithDeviceIds_ReturnsEvents() {
        Zone zone = new Zone("Home", "Address", "home", 300, null);
        zone.setId(10L);
        zone.setLatitude(52.2297);
        zone.setLongitude(21.0122);
        zone.setDeviceIds(List.of("1", "2"));
        when(zoneRepository.findById(10L)).thenReturn(Optional.of(zone));

        List<GeofenceEventDto> events = geofencingService.generateMockEvents(10L, 3);

        assertEquals(3, events.size());
        assertTrue(events.stream().allMatch(e -> e.zoneId().equals(10L)));
        verify(zoneRepository).findById(10L);
    }

    @Test
    void generateMockEvents_WithoutDevices_UsesVirtualDevice() {
        Zone zone = new Zone("Home", "Address", "home", 300, null);
        zone.setId(11L);
        zone.setLatitude(52.2297);
        zone.setLongitude(21.0122);
        when(zoneRepository.findById(11L)).thenReturn(Optional.of(zone));

        List<GeofenceEventDto> events = geofencingService.generateMockEvents(11L, 1);

        assertEquals(1, events.size());
        assertTrue(events.get(0).deviceId().startsWith("virtual-"));
    }

    @Test
    void generateMockEvents_WithUnknownZone_Throws() {
        when(zoneRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> geofencingService.generateMockEvents(99L, 1));
    }
}



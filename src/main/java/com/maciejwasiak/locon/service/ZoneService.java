package com.maciejwasiak.locon.service;

import com.maciejwasiak.locon.dto.ZoneDto;
import com.maciejwasiak.locon.model.Zone;
import com.maciejwasiak.locon.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoneService {
    
    private final ZoneRepository zoneRepository;
    
    @Transactional(readOnly = true)
    public List<ZoneDto> getAllZones() {
        log.info("Fetching all zones");
        return zoneRepository.findAll().stream()
                .map(ZoneDto::from)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public Optional<ZoneDto> getZoneById(Long id) {
        log.info("Fetching zone with id: {}", id);
        return zoneRepository.findById(id)
                .map(ZoneDto::from);
    }
    
    @Transactional
    public ZoneDto createZone(ZoneDto zoneDto) {
        log.info("Creating new zone: {}", zoneDto.name());
        Zone zone = zoneDto.toEntity();
        Zone savedZone = zoneRepository.save(zone);
        log.info("Zone created with id: {}", savedZone.getId());
        return ZoneDto.from(savedZone);
    }
    
    @Transactional
    public Optional<ZoneDto> updateZone(Long id, ZoneDto zoneDto) {
        log.info("Updating zone with id: {}", id);
        return zoneRepository.findById(id)
                .map(existingZone -> {
                    existingZone.setName(zoneDto.name());
                    existingZone.setIcon(zoneDto.icon());
                    existingZone.setAddress(zoneDto.address());
                    existingZone.setLatitude(zoneDto.latitude());
                    existingZone.setLongitude(zoneDto.longitude());
                    existingZone.setRadius(zoneDto.radius());
                    
                    Zone savedZone = zoneRepository.save(existingZone);
                    log.info("Zone updated successfully with id: {}", savedZone.getId());
                    return ZoneDto.from(savedZone);
                });
    }
    
    @Transactional
    public boolean deleteZone(Long id) {
        log.info("Deleting zone with id: {}", id);
        if (zoneRepository.existsById(id)) {
            zoneRepository.deleteById(id);
            log.info("Zone deleted successfully with id: {}", id);
            return true;
        }
        log.warn("Zone with id {} not found for deletion", id);
        return false;
    }
}

package com.maciejwasiak.locon.repository;

import com.maciejwasiak.locon.model.Device;
import com.maciejwasiak.locon.model.Zone;
import com.maciejwasiak.locon.model.ZoneDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneDeviceRepository extends JpaRepository<ZoneDevice, Long> {
    List<ZoneDevice> findByZone(Zone zone);
    List<ZoneDevice> findByDevice(Device device);
    List<ZoneDevice> findByZoneId(Long zoneId);
    List<ZoneDevice> findByDeviceId(Long deviceId);
    Optional<ZoneDevice> findByZoneAndDevice(Zone zone, Device device);
    boolean existsByZoneAndDevice(Zone zone, Device device);
}

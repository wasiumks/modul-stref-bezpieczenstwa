package com.maciejwasiak.locon.service;

import com.maciejwasiak.locon.dto.DeviceDto;
import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {
    
    private final DeviceRepository deviceRepository;
    
    @Transactional(readOnly = true)
    public List<DeviceDto> getDevicesByUserId(Long userId) {
        log.debug("Fetching devices for user id: {}", userId);
        List<DeviceDto> devices = deviceRepository.findByUserId(userId).stream()
                .map(DeviceDto::from)
                .toList();
        log.debug("Found {} devices for user id: {}", devices.size(), userId);
        return devices;
    }
    
    @Transactional(readOnly = true)
    public List<DeviceDto> getDevicesByUser(User user) {
        log.debug("Fetching devices for user: {}", user.getPhone());
        List<DeviceDto> devices = deviceRepository.findByUser(user).stream()
                .map(DeviceDto::from)
                .toList();
        log.debug("Found {} devices for user: {}", devices.size(), user.getPhone());
        return devices;
    }
    
    @Transactional(readOnly = true)
    public Optional<DeviceDto> getDeviceById(Long id) {
        log.debug("Fetching device by id: {}", id);
        return deviceRepository.findById(id)
                .map(device -> {
                    log.debug("Device found: {}", device.getModel());
                    return DeviceDto.from(device);
                });
    }
}

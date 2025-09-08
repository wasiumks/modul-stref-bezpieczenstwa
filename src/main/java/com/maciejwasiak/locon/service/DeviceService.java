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
        log.debug("Fetching devices for user id");
        return deviceRepository.findByUserId(userId).stream()
                .map(DeviceDto::from)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public List<DeviceDto> getDevicesByUser(User user) {
        log.debug("Fetching devices for user");
        return deviceRepository.findByUser(user).stream()
                .map(DeviceDto::from)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public Optional<DeviceDto> getDeviceById(Long id) {
        log.debug("Fetching device by id");
        return deviceRepository.findById(id)
                .map(DeviceDto::from);
    }
}

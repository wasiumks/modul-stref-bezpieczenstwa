package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.dto.DeviceDto;
import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.service.DeviceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Slf4j
public class DeviceApiController {
    
    private final DeviceService deviceService;
    
    @GetMapping
    public ResponseEntity<List<DeviceDto>> getUserDevices(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.warn("Unauthorized access to devices endpoint");
            return ResponseEntity.status(401).build();
        }
        
        log.debug("Getting devices for user: {}", user.getPhone());
        List<DeviceDto> devices = deviceService.getDevicesByUser(user);
        log.debug("Found {} devices for user: {}", devices.size(), user.getPhone());
        return ResponseEntity.ok(devices);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long id) {
        log.debug("Getting device by id: {}", id);
        return deviceService.getDeviceById(id)
                .map(device -> {
                    log.debug("Device found: {}", device.model());
                    return ResponseEntity.ok(device);
                })
                .orElseGet(() -> {
                    log.warn("Device not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
}

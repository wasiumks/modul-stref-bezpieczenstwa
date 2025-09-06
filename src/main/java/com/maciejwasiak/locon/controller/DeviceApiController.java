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
            log.warn("GET /api/devices - No user in session");
            return ResponseEntity.status(401).build();
        }
        
        log.info("GET /api/devices - Fetching devices for user: {}", user.getPhone());
        List<DeviceDto> devices = deviceService.getDevicesByUser(user);
        return ResponseEntity.ok(devices);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long id) {
        log.info("GET /api/devices/{} - Fetching device by id", id);
        return deviceService.getDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

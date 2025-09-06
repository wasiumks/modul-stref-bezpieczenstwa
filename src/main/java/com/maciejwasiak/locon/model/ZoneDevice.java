package com.maciejwasiak.locon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zone_devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDevice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
    
    @Column(name = "notifications_enabled", nullable = false)
    private Boolean notificationsEnabled = true;
    
    public ZoneDevice(Zone zone, Device device, Boolean notificationsEnabled) {
        this.zone = zone;
        this.device = device;
        this.notificationsEnabled = notificationsEnabled;
    }
    
    public ZoneDevice(Zone zone, Device device) {
        this.zone = zone;
        this.device = device;
        this.notificationsEnabled = true;
    }
}

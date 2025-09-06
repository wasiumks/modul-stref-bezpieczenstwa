package com.maciejwasiak.locon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zone {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String icon;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(nullable = false)
    private Integer radius; // in meters
    
    public Zone(String name, String icon, String address, Double latitude, Double longitude, Integer radius) {
        this.name = name;
        this.icon = icon;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }
}

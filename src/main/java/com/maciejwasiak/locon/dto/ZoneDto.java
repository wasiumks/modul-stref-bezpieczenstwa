package com.maciejwasiak.locon.dto;

import com.maciejwasiak.locon.model.Zone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ZoneDto(
    Long id,
    @NotBlank(message = "Zone name is required")
    @Size(max = 100, message = "Zone name must not exceed 100 characters")
    String name,
    
    @NotBlank(message = "Icon is required")
    String icon,
    
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    String address,
    
    @NotNull(message = "Latitude is required")
    Double latitude,
    
    @NotNull(message = "Longitude is required")
    Double longitude,
    
    @NotNull(message = "Radius is required")
    @Positive(message = "Radius must be positive")
    Integer radius,
    
    @NotNull(message = "Notifications enabled status is required")
    Boolean notificationsEnabled
) {
    public static ZoneDto from(Zone zone) {
        return new ZoneDto(
            zone.getId(),
            zone.getName(),
            zone.getIcon(),
            zone.getAddress(),
            zone.getLatitude(),
            zone.getLongitude(),
            zone.getRadius(),
            zone.getNotificationsEnabled()
        );
    }
    
    public Zone toEntity() {
        return new Zone(name, icon, address, latitude, longitude, radius);
    }
}

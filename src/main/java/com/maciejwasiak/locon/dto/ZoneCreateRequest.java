package com.maciejwasiak.locon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ZoneCreateRequest(
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
    Integer radius
) {
    public ZoneDto toZoneDto() {
        return new ZoneDto(null, name, icon, address, latitude, longitude, radius);
    }
}

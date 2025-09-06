package com.maciejwasiak.locon.dto;

import com.maciejwasiak.locon.model.Device;

public record DeviceDto(
    Long id,
    String type,
    String model,
    String ownerName,
    Long userId
) {
    public static DeviceDto from(Device device) {
        return new DeviceDto(
            device.getId(),
            device.getType(),
            device.getModel(),
            device.getOwnerName(),
            device.getUser() != null ? device.getUser().getId() : null
        );
    }
}

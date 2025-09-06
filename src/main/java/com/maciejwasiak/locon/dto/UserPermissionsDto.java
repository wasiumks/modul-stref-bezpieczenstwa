package com.maciejwasiak.locon.dto;

import com.maciejwasiak.locon.model.UserRole;

public record UserPermissionsDto(
    String role,
    String phone
) {
    public static UserPermissionsDto from(UserRole role, String phone) {
        return new UserPermissionsDto(role.name(), phone);
    }
}

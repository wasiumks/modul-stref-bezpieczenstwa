package com.maciejwasiak.locon.dto;

import com.maciejwasiak.locon.model.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPermissionsDtoTest {

    @Test
    void testUserPermissionsDtoCreation() {
        // Given
        String role = "ADMIN";
        String phone = "+48123456789";

        // When
        UserPermissionsDto dto = new UserPermissionsDto(role, phone);

        // Then
        assertEquals(role, dto.role());
        assertEquals(phone, dto.phone());
    }

    @Test
    void testFromMethod_WithAdminRole() {
        // Given
        UserRole role = UserRole.ADMIN;
        String phone = "+48123456789";

        // When
        UserPermissionsDto dto = UserPermissionsDto.from(role, phone);

        // Then
        assertEquals("ADMIN", dto.role());
        assertEquals(phone, dto.phone());
    }

    @Test
    void testFromMethod_WithUserRole() {
        // Given
        UserRole role = UserRole.USER;
        String phone = "+48987654321";

        // When
        UserPermissionsDto dto = UserPermissionsDto.from(role, phone);

        // Then
        assertEquals("USER", dto.role());
        assertEquals(phone, dto.phone());
    }

    @Test
    void testFromMethod_WithViewerRole() {
        // Given
        UserRole role = UserRole.VIEWER;
        String phone = "+48555666777";

        // When
        UserPermissionsDto dto = UserPermissionsDto.from(role, phone);

        // Then
        assertEquals("VIEWER", dto.role());
        assertEquals(phone, dto.phone());
    }

    @Test
    void testUserPermissionsDtoEquality() {
        // Given
        String role = "ADMIN";
        String phone = "+48123456789";
        UserPermissionsDto dto1 = new UserPermissionsDto(role, phone);
        UserPermissionsDto dto2 = new UserPermissionsDto(role, phone);

        // When & Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testUserPermissionsDtoInequality() {
        // Given
        UserPermissionsDto dto1 = new UserPermissionsDto("ADMIN", "+48123456789");
        UserPermissionsDto dto2 = new UserPermissionsDto("USER", "+48123456789");
        UserPermissionsDto dto3 = new UserPermissionsDto("ADMIN", "+48987654321");

        // When & Then
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testUserPermissionsDtoToString() {
        // Given
        String role = "ADMIN";
        String phone = "+48123456789";
        UserPermissionsDto dto = new UserPermissionsDto(role, phone);

        // When
        String toString = dto.toString();

        // Then
        assertTrue(toString.contains("UserPermissionsDto"));
        assertTrue(toString.contains(role));
        assertTrue(toString.contains(phone));
    }

    @Test
    void testFromMethodWithAllRoles() {
        // Test all possible roles
        UserRole[] roles = {UserRole.ADMIN, UserRole.USER, UserRole.VIEWER};
        String phone = "+48123456789";

        for (UserRole role : roles) {
            // When
            UserPermissionsDto dto = UserPermissionsDto.from(role, phone);

            // Then
            assertEquals(role.name(), dto.role());
            assertEquals(phone, dto.phone());
        }
    }
}

package com.maciejwasiak.locon.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    void testUserRoleValues() {
        // Test that all expected roles exist
        UserRole[] roles = UserRole.values();
        assertEquals(3, roles.length);
        
        assertTrue(java.util.Arrays.asList(roles).contains(UserRole.ADMIN));
        assertTrue(java.util.Arrays.asList(roles).contains(UserRole.USER));
        assertTrue(java.util.Arrays.asList(roles).contains(UserRole.VIEWER));
    }

    @Test
    void testUserRoleValueOf() {
        // Test valueOf method
        assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
        assertEquals(UserRole.USER, UserRole.valueOf("USER"));
        assertEquals(UserRole.VIEWER, UserRole.valueOf("VIEWER"));
    }

    @Test
    void testUserRoleToString() {
        // Test toString method
        assertEquals("ADMIN", UserRole.ADMIN.toString());
        assertEquals("USER", UserRole.USER.toString());
        assertEquals("VIEWER", UserRole.VIEWER.toString());
    }
}

package com.maciejwasiak.locon.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testDefaultConstructor() {
        User newUser = new User();
        assertNull(newUser.getId());
        assertNull(newUser.getPhone());
        assertNull(newUser.getRole());
        assertNull(newUser.getOtpCode());
        assertNull(newUser.getOtpExpiresAt());
    }

    @Test
    void testParameterizedConstructor() {
        String phone = "+48123456789";
        UserRole role = UserRole.ADMIN;
        
        User newUser = new User(phone, role);
        
        assertEquals(phone, newUser.getPhone());
        assertEquals(role, newUser.getRole());
        assertNull(newUser.getId());
        assertNull(newUser.getOtpCode());
        assertNull(newUser.getOtpExpiresAt());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String phone = "+48123456789";
        UserRole role = UserRole.USER;
        String otpCode = "123456";
        LocalDateTime otpExpiresAt = LocalDateTime.now().plusMinutes(5);
        
        User newUser = new User(id, phone, role, otpCode, otpExpiresAt);
        
        assertEquals(id, newUser.getId());
        assertEquals(phone, newUser.getPhone());
        assertEquals(role, newUser.getRole());
        assertEquals(otpCode, newUser.getOtpCode());
        assertEquals(otpExpiresAt, newUser.getOtpExpiresAt());
    }

    @Test
    void testSettersAndGetters() {
        // Test ID
        Long id = 1L;
        user.setId(id);
        assertEquals(id, user.getId());

        // Test phone
        String phone = "+48987654321";
        user.setPhone(phone);
        assertEquals(phone, user.getPhone());

        // Test role
        UserRole role = UserRole.ADMIN;
        user.setRole(role);
        assertEquals(role, user.getRole());

        // Test OTP code
        String otpCode = "654321";
        user.setOtpCode(otpCode);
        assertEquals(otpCode, user.getOtpCode());

        // Test OTP expiration
        LocalDateTime otpExpiresAt = LocalDateTime.now().plusMinutes(10);
        user.setOtpExpiresAt(otpExpiresAt);
        assertEquals(otpExpiresAt, user.getOtpExpiresAt());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("+48123456789", UserRole.ADMIN);
        User user2 = new User("+48123456789", UserRole.ADMIN);
        User user3 = new User("+48987654321", UserRole.USER);

        // Test equals
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);

        // Test hashCode
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testToString() {
        user.setId(1L);
        user.setPhone("+48123456789");
        user.setRole(UserRole.ADMIN);
        user.setOtpCode("123456");
        user.setOtpExpiresAt(LocalDateTime.of(2024, 1, 1, 12, 0));

        String toString = user.toString();
        assertTrue(toString.contains("User"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("phone=+48123456789"));
        assertTrue(toString.contains("role=ADMIN"));
        assertTrue(toString.contains("otpCode=123456"));
    }
}

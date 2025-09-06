package com.maciejwasiak.locon.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidLoginRequest() {
        // Given
        String validPhone = "+48123456789";
        LoginRequest request = new LoginRequest(validPhone);

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
        assertEquals(validPhone, request.phone());
    }

    @Test
    void testLoginRequestWithNullPhone_ShouldHaveViolations() {
        // Given
        LoginRequest request = new LoginRequest(null);

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Phone number is required")));
    }

    @Test
    void testLoginRequestWithEmptyPhone_ShouldHaveViolations() {
        // Given
        LoginRequest request = new LoginRequest("");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Phone number is required")));
    }

    @Test
    void testLoginRequestWithInvalidPhoneFormat_ShouldHaveViolations() {
        // Given
        String[] invalidPhones = {
            "123456789",           // Missing +
            "+1234567890123456",   // Too long
            "+0123456789",         // Starts with 0
            "invalid",             // Not a number
            "+48123456789a"        // Contains letters
        };

        for (String invalidPhone : invalidPhones) {
            // When
            LoginRequest request = new LoginRequest(invalidPhone);
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty(), "Should have violations for phone: " + invalidPhone);
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Invalid phone number format")),
                "Should have format violation for phone: " + invalidPhone);
        }
    }

    @Test
    void testLoginRequestWithValidPhoneFormats_ShouldNotHaveViolations() {
        // Given
        String[] validPhones = {
            "+48123456789",
            "+1234567890",
            "+44123456789",
            "+33123456789",
            "+123"  // This should be valid according to the regex
        };

        for (String validPhone : validPhones) {
            // When
            LoginRequest request = new LoginRequest(validPhone);
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertTrue(violations.isEmpty(), "Should not have violations for valid phone: " + validPhone);
        }
    }

    @Test
    void testLoginRequestEquality() {
        // Given
        String phone = "+48123456789";
        LoginRequest request1 = new LoginRequest(phone);
        LoginRequest request2 = new LoginRequest(phone);

        // When & Then
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testLoginRequestToString() {
        // Given
        String phone = "+48123456789";
        LoginRequest request = new LoginRequest(phone);

        // When
        String toString = request.toString();

        // Then
        assertTrue(toString.contains("LoginRequest"));
        assertTrue(toString.contains(phone));
    }
}

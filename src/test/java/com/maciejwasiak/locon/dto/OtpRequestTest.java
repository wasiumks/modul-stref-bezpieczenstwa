package com.maciejwasiak.locon.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OtpRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidOtpRequest() {
        // Given
        String validPhone = "+48123456789";
        String validOtp = "123456";
        OtpRequest request = new OtpRequest(validPhone, validOtp);

        // When
        Set<ConstraintViolation<OtpRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty());
        assertEquals(validPhone, request.phone());
        assertEquals(validOtp, request.otp());
    }

    @Test
    void testOtpRequestWithNullPhone_ShouldHaveViolations() {
        // Given
        OtpRequest request = new OtpRequest(null, "123456");

        // When
        Set<ConstraintViolation<OtpRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Phone number is required")));
    }

    @Test
    void testOtpRequestWithNullOtp_ShouldHaveViolations() {
        // Given
        OtpRequest request = new OtpRequest("+48123456789", null);

        // When
        Set<ConstraintViolation<OtpRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("OTP code is required")));
    }

    @Test
    void testOtpRequestWithEmptyOtp_ShouldHaveViolations() {
        // Given
        OtpRequest request = new OtpRequest("+48123456789", "");

        // When
        Set<ConstraintViolation<OtpRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("OTP code is required")));
    }

    @Test
    void testOtpRequestWithInvalidPhoneFormat_ShouldHaveViolations() {
        // Given
        String[] invalidPhones = {
            "123456789",           // Missing +
            "+1234567890123456",   // Too long
            "+0123456789",         // Starts with 0
            "invalid"              // Not a number
        };

        for (String invalidPhone : invalidPhones) {
            // When
            OtpRequest request = new OtpRequest(invalidPhone, "123456");
            Set<ConstraintViolation<OtpRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty(), "Should have violations for phone: " + invalidPhone);
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Invalid phone number format")),
                "Should have format violation for phone: " + invalidPhone);
        }
    }

    @Test
    void testOtpRequestWithInvalidOtpFormat_ShouldHaveViolations() {
        // Given
        String[] invalidOtps = {
            "12345",      // Too short
            "1234567",    // Too long
            "12345a",     // Contains letters
            "123 456",    // Contains spaces
            "12-3456"     // Contains dashes
        };

        for (String invalidOtp : invalidOtps) {
            // When
            OtpRequest request = new OtpRequest("+48123456789", invalidOtp);
            Set<ConstraintViolation<OtpRequest>> violations = validator.validate(request);

            // Then
            assertFalse(violations.isEmpty(), "Should have violations for OTP: " + invalidOtp);
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("OTP must be 6 digits")),
                "Should have format violation for OTP: " + invalidOtp);
        }
    }

    @Test
    void testOtpRequestWithValidOtpFormats_ShouldNotHaveViolations() {
        // Given
        String[] validOtps = {
            "123456",
            "000000",
            "999999",
            "654321"
        };

        for (String validOtp : validOtps) {
            // When
            OtpRequest request = new OtpRequest("+48123456789", validOtp);
            Set<ConstraintViolation<OtpRequest>> violations = validator.validate(request);

            // Then
            assertTrue(violations.isEmpty(), "Should not have violations for valid OTP: " + validOtp);
        }
    }

    @Test
    void testOtpRequestEquality() {
        // Given
        String phone = "+48123456789";
        String otp = "123456";
        OtpRequest request1 = new OtpRequest(phone, otp);
        OtpRequest request2 = new OtpRequest(phone, otp);

        // When & Then
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testOtpRequestToString() {
        // Given
        String phone = "+48123456789";
        String otp = "123456";
        OtpRequest request = new OtpRequest(phone, otp);

        // When
        String toString = request.toString();

        // Then
        assertTrue(toString.contains("OtpRequest"));
        assertTrue(toString.contains(phone));
        assertTrue(toString.contains(otp));
    }
}

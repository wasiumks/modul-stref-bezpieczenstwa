package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.dto.LoginRequest;
import com.maciejwasiak.locon.dto.OtpRequest;
import com.maciejwasiak.locon.dto.UserPermissionsDto;
import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.UserRole;
import com.maciejwasiak.locon.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private AuthController authController;

    private User testUser;
    private String testPhone;
    private String testOtp;

    @BeforeEach
    void setUp() {
        testPhone = "+48123456789";
        testOtp = "123456";
        testUser = new User(testPhone, UserRole.ADMIN);
    }

    @Test
    void testLoginPage_ShouldReturnLoginTemplate() {
        // When
        String result = authController.loginPage(model);

        // Then
        assertEquals("auth/login-simple", result);
        verify(model).addAttribute("pageTitle", "Logowanie");
    }

    @Test
    void testSendOtp_WithValidRequest_ShouldReturnSuccess() {
        // Given
        LoginRequest request = new LoginRequest(testPhone);
        when(authService.generateOtp(testPhone)).thenReturn(testOtp);

        // When
        ResponseEntity<Map<String, String>> response = authController.sendOtp(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
        assertTrue(response.getBody().containsKey("otp"));
        assertEquals("OTP sent successfully", response.getBody().get("message"));
        assertEquals(testOtp, response.getBody().get("otp"));
        verify(authService).generateOtp(testPhone);
    }

    @Test
    void testSendOtp_WithServiceException_ShouldReturnError() {
        // Given
        LoginRequest request = new LoginRequest(testPhone);
        when(authService.generateOtp(testPhone)).thenThrow(new RuntimeException("Service error"));

        // When
        ResponseEntity<Map<String, String>> response = authController.sendOtp(request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Failed to send OTP", response.getBody().get("error"));
        verify(authService).generateOtp(testPhone);
    }

    @Test
    void testVerifyOtp_WithValidOtp_ShouldReturnSuccess() {
        // Given
        OtpRequest request = new OtpRequest(testPhone, testOtp);
        when(authService.validateOtp(testPhone, testOtp)).thenReturn(true);
        when(authService.getUserByPhone(testPhone)).thenReturn(testUser);

        // When
        ResponseEntity<Map<String, Object>> response = authController.verifyOtp(request, session);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Login successful", response.getBody().get("message"));
        assertEquals("/zones", response.getBody().get("redirectUrl"));
        
        verify(authService).validateOtp(testPhone, testOtp);
        verify(authService).getUserByPhone(testPhone);
        verify(session).setAttribute("user", testUser);
        verify(session).setAttribute("userRole", "ADMIN");
        verify(session).setAttribute("userPhone", testPhone);
    }

    @Test
    void testVerifyOtp_WithInvalidOtp_ShouldReturnError() {
        // Given
        OtpRequest request = new OtpRequest(testPhone, "wrong_otp");
        when(authService.validateOtp(testPhone, "wrong_otp")).thenReturn(false);

        // When
        ResponseEntity<Map<String, Object>> response = authController.verifyOtp(request, session);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Invalid OTP", response.getBody().get("error"));
        
        verify(authService).validateOtp(testPhone, "wrong_otp");
        verify(authService, never()).getUserByPhone(anyString());
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    void testVerifyOtp_WithValidOtpButUserNotFound_ShouldReturnError() {
        // Given
        OtpRequest request = new OtpRequest(testPhone, testOtp);
        when(authService.validateOtp(testPhone, testOtp)).thenReturn(true);
        when(authService.getUserByPhone(testPhone)).thenReturn(null);

        // When
        ResponseEntity<Map<String, Object>> response = authController.verifyOtp(request, session);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("User not found", response.getBody().get("error"));
        
        verify(authService).validateOtp(testPhone, testOtp);
        verify(authService).getUserByPhone(testPhone);
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    void testVerifyOtp_WithServiceException_ShouldReturnError() {
        // Given
        OtpRequest request = new OtpRequest(testPhone, testOtp);
        when(authService.validateOtp(testPhone, testOtp)).thenThrow(new RuntimeException("Service error"));

        // When
        ResponseEntity<Map<String, Object>> response = authController.verifyOtp(request, session);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Failed to verify OTP", response.getBody().get("error"));
        
        verify(authService).validateOtp(testPhone, testOtp);
        verify(authService, never()).getUserByPhone(anyString());
    }

    @Test
    void testLogout_ShouldInvalidateSessionAndReturnSuccess() {
        // When
        ResponseEntity<Map<String, String>> response = authController.logout(session);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("message"));
        assertTrue(response.getBody().containsKey("redirectUrl"));
        assertEquals("Logged out successfully", response.getBody().get("message"));
        assertEquals("/auth/login", response.getBody().get("redirectUrl"));
        verify(session).invalidate();
    }

    @Test
    void testGetUserPermissions_WithUserInSession_ShouldReturnUserPermissions() {
        // Given
        when(session.getAttribute("userRole")).thenReturn("ADMIN");
        when(session.getAttribute("userPhone")).thenReturn(testPhone);

        // When
        ResponseEntity<UserPermissionsDto> response = authController.getUserPermissions(session);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ADMIN", response.getBody().role());
        assertEquals(testPhone, response.getBody().phone());
    }

    @Test
    void testGetUserPermissions_WithNoUserInSession_ShouldReturnDefaultPermissions() {
        // Given
        when(session.getAttribute("userRole")).thenReturn(null);
        when(session.getAttribute("userPhone")).thenReturn(null);

        // When
        ResponseEntity<UserPermissionsDto> response = authController.getUserPermissions(session);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VIEWER", response.getBody().role());
        assertEquals("anonymous", response.getBody().phone());
    }

    @Test
    void testGetUserPermissions_WithPartialSessionData_ShouldReturnDefaultPermissions() {
        // Given
        when(session.getAttribute("userRole")).thenReturn("ADMIN");
        when(session.getAttribute("userPhone")).thenReturn(null);

        // When
        ResponseEntity<UserPermissionsDto> response = authController.getUserPermissions(session);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VIEWER", response.getBody().role());
        assertEquals("anonymous", response.getBody().phone());
    }
}

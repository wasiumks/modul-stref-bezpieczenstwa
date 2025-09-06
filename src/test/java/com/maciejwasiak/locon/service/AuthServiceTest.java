package com.maciejwasiak.locon.service;

import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.UserRole;
import com.maciejwasiak.locon.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private String testPhone;
    private User testUser;

    @BeforeEach
    void setUp() {
        testPhone = "+48123456789";
        testUser = new User(testPhone, UserRole.ADMIN);
    }

    @Test
    void testGenerateOtp_WhenUserExists_ShouldUpdateUser() {
        // Given
        when(userRepository.findByPhone(testPhone)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        String otp = authService.generateOtp(testPhone);

        // Then
        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"));
        
        verify(userRepository).findByPhone(testPhone);
        verify(userRepository).save(testUser);
        assertEquals(otp, testUser.getOtpCode());
        assertNotNull(testUser.getOtpExpiresAt());
        assertTrue(testUser.getOtpExpiresAt().isAfter(LocalDateTime.now()));
    }

    @Test
    void testGenerateOtp_WhenUserDoesNotExist_ShouldCreateNewUser() {
        // Given
        when(userRepository.findByPhone(testPhone)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        String otp = authService.generateOtp(testPhone);

        // Then
        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"));
        
        verify(userRepository).findByPhone(testPhone);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testValidateOtp_WithValidOtp_ShouldReturnTrue() {
        // Given
        String otp = "123456";
        testUser.setOtpCode(otp);
        testUser.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
        
        when(userRepository.findByPhone(testPhone)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        boolean isValid = authService.validateOtp(testPhone, otp);

        // Then
        assertTrue(isValid);
        verify(userRepository).findByPhone(testPhone);
        verify(userRepository).save(testUser);
        assertNull(testUser.getOtpCode());
        assertNull(testUser.getOtpExpiresAt());
    }

    @Test
    void testValidateOtp_WithInvalidOtp_ShouldReturnFalse() {
        // Given
        String correctOtp = "123456";
        String wrongOtp = "654321";
        testUser.setOtpCode(correctOtp);
        testUser.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
        
        when(userRepository.findByPhone(testPhone)).thenReturn(Optional.of(testUser));

        // When
        boolean isValid = authService.validateOtp(testPhone, wrongOtp);

        // Then
        assertFalse(isValid);
        verify(userRepository).findByPhone(testPhone);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testValidateOtp_WithExpiredOtp_ShouldReturnFalse() {
        // Given
        String otp = "123456";
        testUser.setOtpCode(otp);
        testUser.setOtpExpiresAt(LocalDateTime.now().minusMinutes(1)); // Expired
        
        when(userRepository.findByPhone(testPhone)).thenReturn(Optional.of(testUser));

        // When
        boolean isValid = authService.validateOtp(testPhone, otp);

        // Then
        assertFalse(isValid);
        verify(userRepository).findByPhone(testPhone);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testValidateOtp_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Given
        when(userRepository.findByPhone(testPhone)).thenReturn(Optional.empty());

        // When
        boolean isValid = authService.validateOtp(testPhone, "123456");

        // Then
        assertFalse(isValid);
        verify(userRepository).findByPhone(testPhone);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserByPhone_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByPhone(testPhone)).thenReturn(Optional.of(testUser));

        // When
        User foundUser = authService.getUserByPhone(testPhone);

        // Then
        assertNotNull(foundUser);
        assertEquals(testPhone, foundUser.getPhone());
        assertEquals(UserRole.ADMIN, foundUser.getRole());
        verify(userRepository).findByPhone(testPhone);
    }

    @Test
    void testGetUserByPhone_WhenUserDoesNotExist_ShouldReturnNull() {
        // Given
        when(userRepository.findByPhone(testPhone)).thenReturn(Optional.empty());

        // When
        User foundUser = authService.getUserByPhone(testPhone);

        // Then
        assertNull(foundUser);
        verify(userRepository).findByPhone(testPhone);
    }

    @Test
    void testCreateDefaultUsers_WhenUsersDoNotExist_ShouldCreateUsers() {
        // Given
        when(userRepository.findByPhone("+48123456789")).thenReturn(Optional.empty());
        when(userRepository.findByPhone("+48987654321")).thenReturn(Optional.empty());
        when(userRepository.findByPhone("+48555666777")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // When
        authService.createDefaultUsers();

        // Then
        verify(userRepository).findByPhone("+48123456789");
        verify(userRepository).findByPhone("+48987654321");
        verify(userRepository).findByPhone("+48555666777");
        verify(userRepository, times(3)).save(any(User.class));
    }

    @Test
    void testCreateDefaultUsers_WhenUsersAlreadyExist_ShouldNotCreateUsers() {
        // Given
        when(userRepository.findByPhone(anyString())).thenReturn(Optional.of(testUser));

        // When
        authService.createDefaultUsers();

        // Then
        verify(userRepository).findByPhone("+48123456789");
        verify(userRepository).findByPhone("+48987654321");
        verify(userRepository).findByPhone("+48555666777");
        verify(userRepository, never()).save(any(User.class));
    }
}

package com.maciejwasiak.locon.service;

import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.UserRole;
import com.maciejwasiak.locon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final Random random = new Random();
    
    public String generateOtp(String phone) {
        log.info("=== OTP GENERATION STARTED ===");
        log.info("Input phone: {}", phone);
        
        // Generate 6-digit OTP
        String otp = String.format("%06d", random.nextInt(1000000));
        log.info("Generated OTP: {}", otp);
        
        // Find or create user
        User user = userRepository.findByPhone(phone)
                .orElse(new User(phone, UserRole.USER));
        
        // Set OTP and expiration (5 minutes from now)
        user.setOtpCode(otp);
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
        
        userRepository.save(user);
        
        // Log OTP for development purposes
        log.info("=== OTP GENERATED ===");
        log.info("Phone: {}", phone);
        log.info("OTP Code: {}", otp);
        log.info("Expires at: {}", user.getOtpExpiresAt());
        log.info("===================");
        
        // Also print to console for better visibility
        System.out.println("=== OTP GENERATED ===");
        System.out.println("Phone: " + phone);
        System.out.println("OTP Code: " + otp);
        System.out.println("Expires at: " + user.getOtpExpiresAt());
        System.out.println("===================");
        
        return otp;
    }
    
    public boolean validateOtp(String phone, String otp) {
        return userRepository.findByPhone(phone)
                .map(user -> {
                    if (user.getOtpCode() != null && 
                        user.getOtpCode().equals(otp) && 
                        user.getOtpExpiresAt() != null && 
                        user.getOtpExpiresAt().isAfter(LocalDateTime.now())) {
                        
                        // Clear OTP after successful validation
                        user.setOtpCode(null);
                        user.setOtpExpiresAt(null);
                        userRepository.save(user);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }
    
    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }
    
    public void createDefaultUsers() {
        // Create default users if they don't exist
        if (userRepository.findByPhone("+48123456789").isEmpty()) {
            userRepository.save(new User("+48123456789", UserRole.ADMIN));
        }
        if (userRepository.findByPhone("+48987654321").isEmpty()) {
            userRepository.save(new User("+48987654321", UserRole.USER));
        }
        if (userRepository.findByPhone("+48555666777").isEmpty()) {
            userRepository.save(new User("+48555666777", UserRole.VIEWER));
        }
    }
}

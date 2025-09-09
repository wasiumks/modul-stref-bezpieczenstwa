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
        log.debug("Generating OTP for phone: {}", phone);
        String otp = String.format("%06d", random.nextInt(1000000));
        
        User user = userRepository.findByPhone(phone)
                .orElse(new User(phone, UserRole.USER));
        
        user.setOtpCode(otp);
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
        
        userRepository.save(user);
        log.info("OTP generated and saved for phone: {}", phone);
        
        return otp;
    }
    
    public boolean validateOtp(String phone, String otp) {
        log.debug("Validating OTP for phone: {}", phone);
        return userRepository.findByPhone(phone)
                .map(user -> {
                    if (user.getOtpCode() != null && 
                        user.getOtpCode().equals(otp) && 
                        user.getOtpExpiresAt() != null && 
                        user.getOtpExpiresAt().isAfter(LocalDateTime.now())) {
                        
                        user.setOtpCode(null);
                        user.setOtpExpiresAt(null);
                        userRepository.save(user);
                        log.info("OTP validated successfully for phone: {}", phone);
                        return true;
                    }
                    log.warn("Invalid or expired OTP for phone: {}", phone);
                    return false;
                })
                .orElseGet(() -> {
                    log.warn("User not found for OTP validation: {}", phone);
                    return false;
                });
    }
    
    public User getUserByPhone(String phone) {
        log.debug("Getting user by phone: {}", phone);
        return userRepository.findByPhone(phone).orElse(null);
    }
    
    public void createDefaultUsers() {
        log.debug("Creating default users");
        if (userRepository.findByPhone("+48123456789").isEmpty()) {
            userRepository.save(new User("+48123456789", UserRole.ADMIN));
            log.debug("Created admin user");
        }
        if (userRepository.findByPhone("+48987654321").isEmpty()) {
            userRepository.save(new User("+48987654321", UserRole.USER));
            log.debug("Created regular user");
        }
        if (userRepository.findByPhone("+48555666777").isEmpty()) {
            userRepository.save(new User("+48555666777", UserRole.VIEWER));
            log.debug("Created viewer user");
        }
        log.info("Default users creation completed");
    }
}

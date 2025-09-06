package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.dto.LoginRequest;
import com.maciejwasiak.locon.dto.OtpRequest;
import com.maciejwasiak.locon.dto.UserPermissionsDto;
import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthService authService;
    
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("pageTitle", "Logowanie");
        return "auth/login-simple";
    }
    
    @PostMapping("/send-otp")
    @ResponseBody
    public ResponseEntity<Map<String, String>> sendOtp(@Valid @RequestBody LoginRequest request) {
        try {
            String otp = authService.generateOtp(request.phone());
            return ResponseEntity.ok(Map.of(
                "message", "OTP sent successfully",
                "otp", otp // mocking OTP for testing
            ));
        } catch (Exception e) {
            log.error("Error sending OTP", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to send OTP"));
        }
    }
    
    @PostMapping("/verify-otp")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyOtp(@Valid @RequestBody OtpRequest request, HttpSession session) {
        try {
            log.info("=== OTP VERIFICATION STARTED ===");
            log.info("Phone: {}", request.phone());
            log.info("OTP: {}", request.otp());
            
            boolean isValid = authService.validateOtp(request.phone(), request.otp());
            log.info("OTP validation result: {}", isValid);
            
            if (isValid) {
                User user = authService.getUserByPhone(request.phone());
                log.info("User found: {}", user != null ? user.getPhone() + " - " + user.getRole() : "null");
                
                if (user != null) {
                    session.setAttribute("user", user);
                    session.setAttribute("userRole", user.getRole().name());
                    session.setAttribute("userPhone", user.getPhone());
                    
                    log.info("Session attributes set successfully");
                    return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Login successful",
                        "redirectUrl", "/zones"
                    ));
                } else {
                    log.error("User not found after OTP validation");
                    return ResponseEntity.badRequest()
                        .body(Map.of(
                            "success", false,
                            "error", "User not found"
                        ));
                }
            } else {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "success", false,
                        "error", "Invalid OTP"
                    ));
            }
        } catch (Exception e) {
            log.error("Error verifying OTP", e);
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "error", "Failed to verify OTP"
                ));
        }
    }
    
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of(
            "message", "Logged out successfully",
            "redirectUrl", "/auth/login"
        ));
    }
    
    @GetMapping("/permissions")
    @ResponseBody
    public ResponseEntity<UserPermissionsDto> getUserPermissions(HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        String userPhone = (String) session.getAttribute("userPhone");
        
        if (userRole == null || userPhone == null) {
            return ResponseEntity.ok(UserPermissionsDto.from(
                com.maciejwasiak.locon.model.UserRole.VIEWER, 
                "anonymous"
            ));
        }
        
        return ResponseEntity.ok(UserPermissionsDto.from(
            com.maciejwasiak.locon.model.UserRole.valueOf(userRole), 
            userPhone
        ));
    }
}

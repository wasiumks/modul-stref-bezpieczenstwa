package com.maciejwasiak.locon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @Column(name = "otp_code")
    private String otpCode;
    
    @Column(name = "otp_expires_at")
    private java.time.LocalDateTime otpExpiresAt;
    
    public User(String phone, UserRole role) {
        this.phone = phone;
        this.role = role;
    }
}

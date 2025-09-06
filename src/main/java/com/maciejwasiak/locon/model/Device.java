package com.maciejwasiak.locon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private String model;
    
    @Column(name = "owner_name", nullable = false)
    private String ownerName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    public Device(String type, String model, String ownerName, User user) {
        this.type = type;
        this.model = model;
        this.ownerName = ownerName;
        this.user = user;
    }
}

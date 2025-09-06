package com.maciejwasiak.locon.repository;

import com.maciejwasiak.locon.model.Zone;
import com.maciejwasiak.locon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    
    // Find all zones for a specific user
    List<Zone> findByUserOrderByCreatedAtDesc(User user);
    
    // Find a specific zone by ID and user (for security)
    Optional<Zone> findByIdAndUser(Long id, User user);
    
    // Count zones for a user
    long countByUser(User user);
    
    // Find zones by user with pagination
    @Query("SELECT z FROM Zone z WHERE z.user = :user ORDER BY z.createdAt DESC")
    List<Zone> findZonesByUser(@Param("user") User user);
    
    // Check if zone exists by ID and user (for security)
    boolean existsByIdAndUser(Long id, User user);
    
    // Delete zone by ID and user (for security)
    void deleteByIdAndUser(Long id, User user);
}
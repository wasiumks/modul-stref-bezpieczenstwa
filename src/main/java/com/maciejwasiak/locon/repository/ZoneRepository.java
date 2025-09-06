package com.maciejwasiak.locon.repository;

import com.maciejwasiak.locon.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    List<Zone> findByNameContainingIgnoreCase(String name);
}

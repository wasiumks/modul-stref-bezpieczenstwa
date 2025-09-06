package com.maciejwasiak.locon.repository;

import com.maciejwasiak.locon.model.Device;
import com.maciejwasiak.locon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByUser(User user);
    List<Device> findByUserId(Long userId);
}

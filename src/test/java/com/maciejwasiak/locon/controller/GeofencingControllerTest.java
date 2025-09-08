package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.UserRole;
import com.maciejwasiak.locon.model.Zone;
import com.maciejwasiak.locon.repository.UserRepository;
import com.maciejwasiak.locon.repository.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GeofencingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private UserRepository userRepository;

    private Long zoneId;

    @BeforeEach
    void setup() {
        zoneRepository.deleteAll();
        userRepository.deleteAll();

        User user = userRepository.save(new User("+48123456789", UserRole.ADMIN));
        Zone zone = new Zone("Home", "Address", "home", 300, user);
        zone.setLatitude(52.2297);
        zone.setLongitude(21.0122);
        zone.setDeviceIds(List.of("1", "2"));
        zone = zoneRepository.save(zone);
        zoneId = zone.getId();
    }

    @Test
    void getMockEvents_ShouldReturnEvents() throws Exception {
        mockMvc.perform(get("/api/geofence/events/" + zoneId).param("count", "5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].zoneId").value(zoneId));
    }
}



package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.maciejwasiak.locon.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ZonesControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        User persisted = userRepository.findByPhone("+48123456789")
                .orElseGet(() -> userRepository.save(new User("+48123456789", UserRole.ADMIN)));
        session.setAttribute("user", persisted);
    }

    @Test
    void getZones_WithUserSession_ShouldReturnZonesView() throws Exception {
        mockMvc.perform(get("/zones").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("zones"))
                .andExpect(model().attributeExists("pageTitle", "zones", "user"));
    }
}



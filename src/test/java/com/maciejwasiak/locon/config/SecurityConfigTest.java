package com.maciejwasiak.locon.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @Test
    void testPasswordEncoderBean() {
        // Test that password encoder bean is properly configured
        assertNotNull(passwordEncoder);
        
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testSecurityConfiguration() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Test that public endpoints are accessible
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/send-otp")
                .contentType("application/json")
                .content("{\"phone\":\"+48123456789\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/user/permissions"))
                .andExpect(status().isOk());

        // Test that static resources are accessible
        mockMvc.perform(get("/css/output.css"))
                .andExpect(status().isOk());
    }

    @Test
    void testSecurityFilterChainConfiguration() {
        // Test that SecurityConfig is properly loaded
        assertNotNull(webApplicationContext.getBean("filterChain"));
    }
}

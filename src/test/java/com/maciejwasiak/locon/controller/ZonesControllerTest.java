package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.model.User;
import com.maciejwasiak.locon.model.UserRole;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZonesControllerTest {

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    private ZonesController zonesController;
    private User testUser;

    @BeforeEach
    void setUp() {
        zonesController = new ZonesController();
        testUser = new User("+48123456789", UserRole.ADMIN);
    }

    @Test
    void testZones_WithUserInSession_ShouldReturnZonesTemplate() {
        // Given
        when(session.getAttribute("user")).thenReturn(testUser);
        when(session.getId()).thenReturn("test-session-id");
        when(session.getAttributeNames()).thenReturn(java.util.Collections.enumeration(List.of("user", "userRole", "userPhone")));

        // When
        String result = zonesController.zones(null, model, session);

        // Then
        assertEquals("zones-simple", result);
        
        // Verify model attributes
        verify(model).addAttribute("pageTitle", "Strefy Bezpieczeństwa");
        verify(model).addAttribute("pageDescription", "Zarządzaj strefami bezpieczeństwa dla swoich bliskich");
        verify(model).addAttribute("user", testUser);
        verify(model).addAttribute(eq("zones"), any(List.class));
        verify(model).addAttribute("hasZones", true);
    }

    @Test
    void testZones_WithNoUserInSession_ShouldRedirectToLogin() {
        // Given
        when(session.getAttribute("user")).thenReturn(null);
        when(session.getId()).thenReturn("test-session-id");
        when(session.getAttributeNames()).thenReturn(java.util.Collections.emptyEnumeration());

        // When
        String result = zonesController.zones(null, model, session);

        // Then
        assertEquals("redirect:/auth/login", result);
        
        // Verify that no model attributes are set
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void testZones_WithUserInSession_ShouldSetCorrectModelAttributes() {
        // Given
        when(session.getAttribute("user")).thenReturn(testUser);
        when(session.getId()).thenReturn("test-session-id");
        when(session.getAttributeNames()).thenReturn(java.util.Collections.enumeration(List.of("user", "userRole", "userPhone")));

        // When
        zonesController.zones(null, model, session);

        // Then
        verify(model).addAttribute("pageTitle", "Strefy Bezpieczeństwa");
        verify(model).addAttribute("pageDescription", "Zarządzaj strefami bezpieczeństwa dla swoich bliskich");
        verify(model).addAttribute("user", testUser);
        verify(model).addAttribute(eq("zones"), any(List.class));
        verify(model).addAttribute("hasZones", true);
    }

    @Test
    void testZones_WithUserInSession_ShouldContainMockZonesData() {
        // Given
        when(session.getAttribute("user")).thenReturn(testUser);
        when(session.getId()).thenReturn("test-session-id");
        when(session.getAttributeNames()).thenReturn(java.util.Collections.enumeration(List.of("user", "userRole", "userPhone")));

        // When
        zonesController.zones(null, model, session);

        // Then
        verify(model).addAttribute(eq("zones"), argThat(zones -> {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> zonesList = (List<Map<String, Object>>) zones;
            return zonesList.size() == 2 &&
                   zonesList.get(0).get("name").equals("Dom") &&
                   zonesList.get(0).get("address").equals("ul. Przykładowa 123, Warszawa") &&
                   zonesList.get(0).get("icon").equals("home") &&
                   zonesList.get(0).get("devicesCount").equals(2) &&
                   zonesList.get(0).get("radius").equals(500) &&
                   zonesList.get(1).get("name").equals("Szkoła") &&
                   zonesList.get(1).get("address").equals("ul. Szkolna 45, Warszawa") &&
                   zonesList.get(1).get("icon").equals("school") &&
                   zonesList.get(1).get("devicesCount").equals(1) &&
                   zonesList.get(1).get("radius").equals(300);
        }));
    }

    @Test
    void testZones_WithUserInSession_ShouldSetHasZonesToTrue() {
        // Given
        when(session.getAttribute("user")).thenReturn(testUser);
        when(session.getId()).thenReturn("test-session-id");
        when(session.getAttributeNames()).thenReturn(java.util.Collections.enumeration(List.of("user", "userRole", "userPhone")));

        // When
        zonesController.zones(null, model, session);

        // Then
        verify(model).addAttribute("hasZones", true);
    }

    @Test
    void testZones_WithDifferentUserRoles_ShouldWorkCorrectly() {
        // Given
        User userUser = new User("+48987654321", UserRole.USER);
        User viewerUser = new User("+48555666777", UserRole.VIEWER);
        
        when(session.getAttribute("user")).thenReturn(userUser);
        when(session.getId()).thenReturn("test-session-id");
        when(session.getAttributeNames()).thenReturn(java.util.Collections.enumeration(List.of("user", "userRole", "userPhone")));

        // When
        String result = zonesController.zones(null, model, session);

        // Then
        assertEquals("zones-simple", result);
        verify(model).addAttribute("user", userUser);
    }

    @Test
    void testZones_WithNullSession_ShouldRedirectToLogin() {
        // Given
        when(session.getAttribute("user")).thenReturn(null);
        when(session.getId()).thenReturn("test-session-id");
        when(session.getAttributeNames()).thenReturn(java.util.Collections.emptyEnumeration());

        // When
        String result = zonesController.zones(null, model, session);

        // Then
        assertEquals("redirect:/auth/login", result);
        verify(model, never()).addAttribute(anyString(), any());
    }
}

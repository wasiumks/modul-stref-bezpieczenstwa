package com.maciejwasiak.locon.controller;

import com.maciejwasiak.locon.dto.UserPermissionsDto;
import com.maciejwasiak.locon.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {
    
    @GetMapping("/permissions")
    public ResponseEntity<UserPermissionsDto> getUserPermissions(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.warn("GET /api/user/permissions - No user in session");
            return ResponseEntity.status(401).build();
        }
        
        log.info("GET /api/user/permissions - Fetching permissions for user: {}", user.getPhone());
        UserPermissionsDto permissions = UserPermissionsDto.from(user.getRole(), user.getPhone());
        return ResponseEntity.ok(permissions);
    }
}

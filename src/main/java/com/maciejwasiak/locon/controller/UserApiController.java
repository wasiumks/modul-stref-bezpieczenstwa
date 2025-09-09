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
            log.debug("No user in session, returning guest permissions");
            UserPermissionsDto defaultPermissions = new UserPermissionsDto("GUEST", null);
            return ResponseEntity.ok(defaultPermissions);
        }
        
        log.debug("Returning permissions for user: {} with role: {}", user.getPhone(), user.getRole());
        UserPermissionsDto permissions = UserPermissionsDto.from(user.getRole(), user.getPhone());
        return ResponseEntity.ok(permissions);
    }
}

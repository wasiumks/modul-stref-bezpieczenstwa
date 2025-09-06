package com.maciejwasiak.locon.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/")
    public String index(HttpSession session) {
        // Check if user is logged in
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        return "redirect:/zones";
    }
}
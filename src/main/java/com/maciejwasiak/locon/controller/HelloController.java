package com.maciejwasiak.locon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "index"; // Thymeleaf szuka index.html w templates
    }
}
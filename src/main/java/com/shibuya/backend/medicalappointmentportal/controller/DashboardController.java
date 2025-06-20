package com.shibuya.backend.medicalappointmentportal.controller;

import com.shibuya.backend.medicalappointmentportal.Service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class DashboardController {
    @Autowired
    private TokenService tokenValidationService;

    // Admin Dashboard
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        Map<String, Object> validation = tokenValidationService.validateToken(token, "admin");

        if (validation.isEmpty()) {
            // Token is valid
            return "admin/adminDashboard";
        } else {
            // Invalid token, redirect to login
            return "redirect:http://localhost:8080";
        }
    }

    // Doctor Dashboard
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        Map<String, Object> validation = tokenValidationService.validateToken(token, "doctor");

        if (validation.isEmpty()) {
            // Token is valid
            return "doctor/doctorDashboard";
        } else {
            // Invalid token, redirect to login
            return "redirect:http://localhost:8080";
        }
    }
}

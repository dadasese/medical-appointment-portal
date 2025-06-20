package com.shibuya.backend.medicalappointmentportal.controller;

import com.shibuya.backend.medicalappointmentportal.service.DTO.LoginDTO;
import com.shibuya.backend.medicalappointmentportal.model.Patient;
import com.shibuya.backend.medicalappointmentportal.service.PatientService;
import com.shibuya.backend.medicalappointmentportal.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private Service service;

    // 1. Get Patient Details
    @GetMapping("/{token}")
    public ResponseEntity<? extends Map<String,? extends Object>> getPatientDetails(@PathVariable String token) {
        return service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()
                ? patientService.getPatientDetails(token)
                : service.validateToken(token, "patient");
    }

    // 2. Create a New Patient
    @PostMapping()
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        if (!service.validatePatient(patient)) {
            return ResponseEntity.status(409).body(Map.of("message", "Patient with email id or phone no already exist"));
        }
        int result = patientService.createPatient(patient);
        return result == 1
                ? ResponseEntity.status(201).body(Map.of("message", "Signup successful"))
                : ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
    }

    // 3. Patient Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO login) {
        return service.validatePatientLogin(login);
    }

    // 4. Get Patient Appointments
    @GetMapping("/{id}/{token}")
    public ResponseEntity<? extends Map<String,? extends Object>> getPatientAppointments(@PathVariable Long id, @PathVariable String token) {
        return service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()
                ? patientService.getPatientAppointment(id, token)
                : service.validateToken(token, "patient");
    }

    // 5. Filter Patient Appointments
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<? extends Map<String,? extends Object>> filterAppointments(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token
    ) {
        return service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()
                ? service.filterPatient(condition, name, token)
                : service.validateToken(token, "patient");
    }
}

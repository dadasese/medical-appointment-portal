package com.shibuya.backend.medicalappointmentportal.controller;

import com.shibuya.backend.medicalappointmentportal.model.Doctor;
import com.shibuya.backend.medicalappointmentportal.service.DTO.LoginDTO;
import com.shibuya.backend.medicalappointmentportal.service.DoctorService;
import com.shibuya.backend.medicalappointmentportal.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Service service;

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(@PathVariable String user,
                                                                     @PathVariable Long doctorId,
                                                                     @PathVariable String date,
                                                                     @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, user);
        if (validation.getStatusCode().isError()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", validation.getBody().get("error"));
            return ResponseEntity.status(validation.getStatusCode()).body(error);
        }
        List<String> availability = doctorService.getDoctorAvailability(doctorId, LocalDate.parse(date));
        Map<String, Object> response = new HashMap<>();
        response.put("availability", availability);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getDoctors();
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> addDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (validation.getStatusCode().isError()) {
            return validation;
        }
        int result = doctorService.saveDoctor(doctor);
        Map<String, String> response = new HashMap<>();
        if (result == 1) {
            response.put("message", "Doctor added to db");
            return ResponseEntity.status(201).body(response);
        } else if (result == -1) {
            response.put("error", "Doctor already exists");
            return ResponseEntity.status(409).body(response);
        } else {
            response.put("error", "Some internal error occurred");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody LoginDTO login) {
        return doctorService.validateDoctor(login);
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (validation.getStatusCode().isError()) {
            return validation;
        }
        int result = doctorService.updateDoctor(doctor);
        Map<String, String> response = new HashMap<>();
        if (result == 1) {
            response.put("message", "Doctor updated");
            return ResponseEntity.ok(response);
        } else if (result == -1) {
            response.put("error", "Doctor not found");
            return ResponseEntity.status(404).body(response);
        } else {
            response.put("error", "Some internal error occurred");
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable long id, @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
        if (validation.getStatusCode().isError()) {
            return validation;
        }
        int result = doctorService.deleteDoctor(id);
        Map<String, String> response = new HashMap<>();
        if (result == 1) {
            response.put("message", "Doctor deleted successfully");
            return ResponseEntity.ok(response);
        } else if (result == -1) {
            response.put("error", "Doctor not found with id");
            return ResponseEntity.status(404).body(response);
        } else {
            response.put("error", "Some internal error occurred");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(@PathVariable String name,
                                                             @PathVariable String time,
                                                             @PathVariable String speciality) {
        return ResponseEntity.ok(service.filterDoctor(name, speciality, time));
    }
}


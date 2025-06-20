package com.shibuya.backend.medicalappointmentportal.controller;

import com.shibuya.backend.medicalappointmentportal.model.Appointment;
import com.shibuya.backend.medicalappointmentportal.service.AppointmentService;
import com.shibuya.backend.medicalappointmentportal.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private Service service;

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable String date,
                                             @PathVariable String patientName,
                                             @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "doctor");
        if (validation != null) return validation;

        LocalDate appointmentDate = LocalDate.parse(date);
        Map<String, Object> appointments = appointmentService.getAppointment(patientName, appointmentDate, token);
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/{token}")
    public ResponseEntity<?> bookAppointment(@PathVariable String token,
                                             @RequestBody Appointment appointment) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
        if (validation != null) return validation;

        int validationResult = service.validateAppointment(appointment);
        if (validationResult == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Doctor does not exist."));
        } else if (validationResult == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Appointment time is not available."));
        }

        int result = appointmentService.bookAppointment(appointment);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Appointment booked successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to book appointment."));
        }
    }

    @PutMapping("/{token}")
    public ResponseEntity<?> updateAppointment(@PathVariable String token,
                                               @RequestBody Appointment appointment) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
        if (validation != null) return validation;

        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<?> cancelAppointment(@PathVariable long id,
                                               @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
        if (validation != null) return validation;

        return appointmentService.cancelAppointment(id, token);
    }
}